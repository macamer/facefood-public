import { getUser, userIsLogged } from "./user.js";

//menu desplegable
const settingsIcon = document.getElementById("settings-icon");
const settingsMenu = document.getElementById("settings-menu");

if (settingsIcon != null) {
	settingsIcon.addEventListener("click", function(event) {
		event.stopPropagation();

		// Alternar visibilidad del menú
		if (settingsMenu.style.display === "none" || settingsMenu.style.display === "") {
			settingsMenu.style.display = "block";
		} else {
			settingsMenu.style.display = "none";
		}
	});

	// Cerrar el menú si se hace clic fuera de él
	document.addEventListener("click", function() {
		settingsMenu.style.display = "none";
	});

	settingsMenu.addEventListener("click", function(event) {
		event.stopPropagation();
	});

	if (document.getElementById("logout") != null) {
		if (userIsLogged(getUser())) {
			document.getElementById("logout").addEventListener("click", function (e) {
				e.preventDefault();
				sessionStorage.setItem("usuario", "")
				window.location.href = '/facefood/';
			})
		} else {
			document.getElementById("logout").innerHTML = "Iniciar sesión";
			document.getElementById("logout").addEventListener("click", function (e) {
				e.preventDefault();
				window.location.href = '/facefood/login';
			})
		}
	}

	if (document.getElementById("account")!= null && userIsLogged(getUser())) {
		document.getElementById("account").addEventListener("click", function (e) {
			e.preventDefault();
			let usuarioSes = getUser()
			window.location.href = '/facefood/' + usuarioSes.usuario + '/myaccount';
		});
	} else if (document.getElementById("account") != null) {
		document.getElementById("account").innerHTML = "Registrarse";
		document.getElementById("account").addEventListener("click", function (e) {
			e.preventDefault();
			window.location.href = '/facefood/add';
		});
	}
}