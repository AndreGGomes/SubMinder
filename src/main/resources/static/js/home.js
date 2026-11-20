const token = sessionStorage.getItem("subminderToken");
const dashboardStatus = document.querySelector("#dashboard-status");
const dashboardError = document.querySelector("#dashboard-error");
const dashboardErrorWrap = document.querySelector("#dashboard-error-wrap");
const dashboardContent = document.querySelector("#dashboard-content");
const subscriptionDialog = document.querySelector("#subscription-dialog");
const subscriptionForm = document.querySelector("#subscription-form");
const subscriptionFormMessage = document.querySelector("#subscription-form-message");
const subscriptionFeedback = document.querySelector("#subscription-feedback");
let dashboardSubscriptions = [];
let spendingPeriod = "monthly";
const currencyFormatter = new Intl.NumberFormat("pt-BR", {
  style: "currency",
  currency: "BRL",
});
const dateFormatter = new Intl.DateTimeFormat("pt-BR", {
  day: "2-digit",
  month: "short",
  year: "numeric",
});

if (!token) {
  window.location.replace("/");
} else {
  document.querySelector("#retry-dashboard").addEventListener("click", loadDashboard);
  document.querySelector("#add-subscription-button").addEventListener("click", openSubscriptionDialog);
  document.querySelector("#close-subscription-dialog").addEventListener("click", closeSubscriptionDialog);
  document.querySelector("#cancel-subscription").addEventListener("click", closeSubscriptionDialog);
  subscriptionDialog.addEventListener("close", () => {
    subscriptionForm.reset();
    subscriptionFormMessage.textContent = "";
  });
  subscriptionForm.addEventListener("submit", createSubscription);
  document.querySelectorAll(".period-button").forEach((button) => {
    button.addEventListener("click", () => {
      spendingPeriod = button.dataset.period;
      renderSpendingChart(dashboardSubscriptions);
    });
  });

  document.querySelector("#sign-out").addEventListener("click", async () => {
    const message = document.querySelector("#sign-out-message");
    try {
      const response = await fetch("/api/auth/logout", {
        method: "POST",
        headers: { Authorization: `Bearer ${token}` },
      });
      if (!response.ok) {
        throw new Error("Não foi possível sair. Tente novamente.");
      }
      sessionStorage.removeItem("subminderToken");
      window.location.replace("/");
    } catch (error) {
      message.hidden = false;
      message.textContent = error.message;
    }
  });

  loadDashboard();
}

function openSubscriptionDialog() {
  subscriptionFormMessage.textContent = "";
  subscriptionFeedback.hidden = true;
  if (!subscriptionDialog.open) {
    subscriptionDialog.showModal();
  }
  subscriptionForm.elements.name.focus();
}

function closeSubscriptionDialog() {
  subscriptionDialog.close();
}

async function createSubscription(event) {
  event.preventDefault();
  const button = subscriptionForm.querySelector('button[type="submit"]');
  const values = Object.fromEntries(new FormData(subscriptionForm).entries());

  button.disabled = true;
  subscriptionFormMessage.textContent = "";

  try {
    const response = await fetch("/api/subscriptions", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify({
        name: String(values.name).trim(),
        price: Number(values.price),
        cycle: values.cycle,
        nextBillingDate: `${values.nextBillingDate}T00:00`,
      }),
    });
    const body = await response.json().catch(() => ({}));

    if (response.status === 401 || response.status === 403) {
      sessionStorage.removeItem("subminderToken");
      window.location.replace("/");
      return;
    }
    if (!response.ok) {
      throw new Error(
        body.message ||
          body.error ||
          "Não foi possível adicionar a assinatura. Tente novamente.",
      );
    }

    closeSubscriptionDialog();
    subscriptionFeedback.hidden = false;
    subscriptionFeedback.textContent = `${String(values.name).trim()} foi adicionada às suas assinaturas.`;
    await loadDashboard();
  } catch (error) {
    subscriptionFormMessage.textContent = error.message;
  } finally {
    button.disabled = false;
  }
}

async function loadDashboard() {
  dashboardStatus.hidden = false;
  dashboardStatus.textContent = "Carregando suas assinaturas...";
  dashboardErrorWrap.hidden = true;

  try {
    const response = await fetch("/api/subscriptions", {
      headers: { Authorization: `Bearer ${token}` },
    });

    if (response.status === 401 || response.status === 403) {
      sessionStorage.removeItem("subminderToken");
      window.location.replace("/");
      return;
    }
    if (!response.ok) {
      throw new Error("Não foi possível carregar suas assinaturas. Tente novamente.");
    }

    const subscriptions = await response.json();
    if (!Array.isArray(subscriptions)) {
      throw new Error("A resposta recebida não contém uma lista de assinaturas válida.");
    }

    renderDashboard(subscriptions.map(normalizeSubscription));
    dashboardStatus.hidden = true;
    dashboardContent.hidden = false;
  } catch (error) {
    dashboardStatus.hidden = true;
    dashboardErrorWrap.hidden = false;
    dashboardError.textContent = error.message;
  }
}

function normalizeSubscription(subscription) {
  const price = Number(subscription.price);
  const nextBillingDate = new Date(subscription.nextBillingDate);

  if (
    typeof subscription.name !== "string" ||
    !subscription.name.trim() ||
    !Number.isFinite(price) ||
    price <= 0 ||
    !["MONTHLY", "YEARLY"].includes(subscription.cycle) ||
    Number.isNaN(nextBillingDate.getTime())
  ) {
    throw new Error("Uma assinatura recebida contém dados inválidos. Verifique o cadastro.");
  }

  return {
    id: subscription.id,
    name: subscription.name.trim(),
    price,
    cycle: subscription.cycle,
    nextBillingDate,
    monthlyEquivalent: subscription.cycle === "YEARLY" ? price / 12 : price,
  };
}

function renderDashboard(subscriptions) {
  dashboardSubscriptions = subscriptions;
  const monthlyTotal = subscriptions.reduce(
    (total, subscription) => total + subscription.monthlyEquivalent,
    0,
  );
  const yearlyTotal = monthlyTotal * 12;

  document.querySelector("#monthly-total").textContent = formatCurrency(monthlyTotal);
  document.querySelector("#yearly-total").textContent = formatCurrency(yearlyTotal);
  document.querySelector("#subscription-count").textContent = subscriptions.length;
  document.querySelector("#dashboard-updated").textContent =
    `Atualizado em ${new Intl.DateTimeFormat("pt-BR", {
      day: "2-digit",
      month: "long",
      year: "numeric",
    }).format(new Date())}`;

  renderNextBilling(subscriptions);
  renderSpendingChart(subscriptions);
  renderUpcomingPayments(subscriptions);
  renderSubscriptionList(subscriptions);
}

function renderNextBilling(subscriptions) {
  const today = startOfToday();
  const nextSubscription = subscriptions
    .filter((subscription) => subscription.nextBillingDate >= today)
    .sort(
      (first, second) => first.nextBillingDate - second.nextBillingDate,
    )[0];

  document.querySelector("#next-billing-date").textContent = nextSubscription
    ? formatDate(nextSubscription.nextBillingDate)
    : "—";
  document.querySelector("#next-billing-name").textContent = nextSubscription
    ? nextSubscription.name
    : subscriptions.length
      ? "Nenhuma cobrança futura"
      : "Nenhuma cobrança cadastrada";
}

function renderSpendingChart(subscriptions) {
  const chart = document.querySelector("#spending-chart");
  chart.replaceChildren();

  const isMonthly = spendingPeriod === "monthly";
  const periodSpending = (subscription) =>
    isMonthly
      ? subscription.monthlyEquivalent
      : subscription.cycle === "YEARLY"
        ? subscription.price
        : subscription.price * 12;

  document.querySelector("#spending-period-label").textContent = isMonthly
    ? "Distribuição mensal"
    : "Distribuição anual";
  document.querySelector("#spending-period-description").textContent = isMonthly
    ? "Comparativo por serviço, com planos anuais divididos por 12."
    : "Comparativo por serviço, com planos mensais projetados para 12 meses.";
  document.querySelectorAll(".period-button").forEach((button) => {
    const isActive = button.dataset.period === spendingPeriod;
    button.classList.toggle("is-active", isActive);
    button.setAttribute("aria-pressed", String(isActive));
  });
  const periodTotal = subscriptions.reduce(
    (total, subscription) => total + periodSpending(subscription),
    0,
  );
  chart.setAttribute(
    "aria-label",
    `Gráfico de gastos ${isMonthly ? "mensais" : "anuais"} por assinatura. Total estimado: ${formatCurrency(
      periodTotal,
    )}.`,
  );

  if (!subscriptions.length) {
    chart.append(createEmptyState("Cadastre sua primeira assinatura para ver a distribuição."));
    return;
  }

  const sortedSubscriptions = [...subscriptions].sort(
    (first, second) => periodSpending(second) - periodSpending(first),
  );
  const maxSpending = periodSpending(sortedSubscriptions[0]);

  for (const subscription of sortedSubscriptions) {
    const row = document.createElement("div");
    row.className = "chart-row";

    const label = document.createElement("div");
    label.className = "chart-label";
    label.textContent = subscription.name;
    label.title = subscription.name;

    const track = document.createElement("div");
    track.className = "chart-track";
    track.setAttribute("aria-hidden", "true");

    const bar = document.createElement("span");
    bar.className = "chart-bar";
    bar.style.width = `${(periodSpending(subscription) / maxSpending) * 100}%`;
    track.append(bar);

    const amount = document.createElement("span");
    amount.className = "chart-amount";
    amount.textContent = formatCurrency(periodSpending(subscription));

    row.append(label, track, amount);
    chart.append(row);
  }
}

function renderUpcomingPayments(subscriptions) {
  const list = document.querySelector("#upcoming-list");
  list.replaceChildren();

  const today = startOfToday();
  const upcoming = subscriptions
    .filter((subscription) => subscription.nextBillingDate >= today)
    .sort((first, second) => first.nextBillingDate - second.nextBillingDate)
    .slice(0, 5);

  if (!upcoming.length) {
    list.append(
      createEmptyState(
        subscriptions.length
          ? "Não há cobranças futuras nas datas informadas."
          : "Suas próximas cobranças aparecerão aqui.",
      ),
    );
    return;
  }

  for (const subscription of upcoming) {
    const item = document.createElement("li");
    item.className = "upcoming-item";

    const details = document.createElement("div");
    details.className = "upcoming-details";

    const name = document.createElement("span");
    name.className = "upcoming-name";
    name.textContent = subscription.name;

    const date = document.createElement("span");
    date.className = "upcoming-date";
    date.textContent = formatDate(subscription.nextBillingDate);
    details.append(name, date);

    const amount = document.createElement("span");
    amount.className = "upcoming-amount";
    amount.textContent = formatCurrency(subscription.price);

    item.append(details, amount);
    list.append(item);
  }
}

function renderSubscriptionList(subscriptions) {
  const list = document.querySelector("#subscription-list");
  list.replaceChildren();

  const count = subscriptions.length;
  document.querySelector("#subscription-total-label").textContent =
    `${count} ${count === 1 ? "serviço" : "serviços"}`;

  if (!count) {
    list.append(
      createEmptyState("Nenhuma assinatura por enquanto. Seus dados aparecerão aqui quando forem cadastrados."),
    );
    return;
  }

  const sortedSubscriptions = [...subscriptions].sort((first, second) =>
    first.name.localeCompare(second.name, "pt-BR"),
  );

  for (const subscription of sortedSubscriptions) {
    const item = document.createElement("div");
    item.className = "subscription-row";

    const nameGroup = document.createElement("div");
    nameGroup.className = "subscription-name-group";

    const avatar = document.createElement("span");
    avatar.className = "service-avatar";
    avatar.setAttribute("aria-hidden", "true");
    avatar.textContent = subscription.name.slice(0, 1).toLocaleUpperCase("pt-BR");

    const name = document.createElement("span");
    name.className = "subscription-name";
    name.textContent = subscription.name;
    nameGroup.append(avatar, name);

    const cycle = document.createElement("span");
    cycle.className = "cycle-badge";
    cycle.textContent = subscription.cycle === "YEARLY" ? "Anual" : "Mensal";

    const price = document.createElement("span");
    price.className = "subscription-price";
    price.textContent = formatCurrency(subscription.price);

    const nextBilling = document.createElement("span");
    nextBilling.className = "subscription-next-billing";
    nextBilling.textContent = `Próxima: ${formatDate(subscription.nextBillingDate)}`;

    item.append(nameGroup, cycle, price, nextBilling);
    list.append(item);
  }
}

function createEmptyState(message) {
  const empty = document.createElement("p");
  empty.className = "empty-state";
  empty.textContent = message;
  return empty;
}

function formatCurrency(value) {
  return currencyFormatter.format(value);
}

function formatDate(value) {
  return dateFormatter.format(value);
}

function startOfToday() {
  const today = new Date();
  today.setHours(0, 0, 0, 0);
  return today;
}
