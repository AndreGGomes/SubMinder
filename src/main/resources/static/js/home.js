const token = sessionStorage.getItem("subminderToken");

if (!token) {
  window.location.replace("/");
} else {
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
}
