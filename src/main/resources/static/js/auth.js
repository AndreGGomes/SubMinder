const loginTab = document.querySelector("#login-tab");
const registerTab = document.querySelector("#register-tab");
const loginForm = document.querySelector("#login-panel");
const registerForm = document.querySelector("#register-panel");
const heading = document.querySelector("#form-heading");
const description = document.querySelector("#form-description");
const message = document.querySelector("#form-message");
const registerPassword = document.querySelector("#register-password");

registerPassword.addEventListener("input", () => {
  registerPassword.setCustomValidity(
    registerPassword.validity.tooShort
      ? "A senha deve ter pelo menos 8 caracteres."
      : "",
  );
});

function showForm(formName) {
  const isLogin = formName === "login";
  loginForm.hidden = !isLogin;
  registerForm.hidden = isLogin;
  loginTab.classList.toggle("is-active", isLogin);
  registerTab.classList.toggle("is-active", !isLogin);
  loginTab.setAttribute("aria-selected", String(isLogin));
  registerTab.setAttribute("aria-selected", String(!isLogin));
  heading.textContent = isLogin ? "Entre na sua conta" : "Crie sua conta";
  description.textContent = isLogin
    ? "Continue de onde você parou."
    : "Comece a usar o SubMinder em instantes.";
  message.textContent = "";
  message.classList.remove("is-success");
}

function formValues(form) {
  return Object.fromEntries(new FormData(form).entries());
}

async function postJson(path, data) {
  const response = await fetch(path, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(data),
  });
  const body = await response.json();

  if (!response.ok) {
    throw new Error(
      body.message || body.error || "Algo deu errado. Tente novamente.",
    );
  }

  return body;
}

async function signIn(credentials) {
  const result = await postJson("/api/auth/login", credentials);
  if (!result.token) {
    throw new Error("O login foi concluído, mas nenhum token de sessão foi recebido.");
  }
  sessionStorage.setItem("subminderToken", result.token);
  window.location.assign("/home.html");
}

loginTab.addEventListener("click", () => showForm("login"));
registerTab.addEventListener("click", () => showForm("register"));

loginForm.addEventListener("submit", async (event) => {
  event.preventDefault();
  const button = loginForm.querySelector('button[type="submit"]');
  button.disabled = true;
  message.textContent = "";

  try {
    await signIn(formValues(loginForm));
  } catch (error) {
    message.textContent = error.message;
  } finally {
    button.disabled = false;
  }
});

registerForm.addEventListener("submit", async (event) => {
  event.preventDefault();
  const button = registerForm.querySelector('button[type="submit"]');
  const values = formValues(registerForm);
  button.disabled = true;
  message.textContent = "";
  let accountCreated = false;

  try {
    await postJson("/api/auth/register", values);
    accountCreated = true;
    await signIn({ email: values.email, password: values.password });
  } catch (error) {
    if (accountCreated) {
      showForm("login");
      loginForm.elements.email.value = values.email;
      message.classList.add("is-success");
      message.textContent = `Conta criada, mas não foi possível entrar automaticamente: ${error.message}`;
    } else if (error.message.includes("E-mail já está em uso")) {
      showForm("login");
      message.textContent = "Este e-mail já possui uma conta. Entre com ela.";
    } else {
      message.textContent = error.message;
    }
  } finally {
    button.disabled = false;
  }
});
