export function limpiarErrores() {
	document.querySelectorAll(".error-message").forEach(el => el.remove());
	document.querySelectorAll(".error-input").forEach(el => { el.classList.remove("error-input"); el.classList.add("iborder"); });
}

export function mostrarErrores(errors) {
	let firstErrorField = null;

	errors.forEach(error => {
		const field = error.field;
		const message = error.message;

		// Crear elemento de error
		const errorText = document.createElement("p");
		errorText.className = "error-message";
		errorText.textContent = message;

		// Insertar mensaje de error debajo del input
		field.parentNode.appendChild(errorText);

		// Agregar borde rojo al campo con error
		field.classList.remove("iborder");
		field.classList.add("error-input")

		// Guardar el primer campo con error para hacer focus
		if (!firstErrorField) {
			firstErrorField = field;
		}
	});

	// Hacer focus en el primer campo con error
	if (firstErrorField) {
		firstErrorField.focus();
	}
}

export function limpiarStorage() {
	sessionStorage.setItem("nombreUsuario", "");
	sessionStorage.setItem("idUsuario", "");
	sessionStorage.setItem("rol", "");
	window.location.href = "index.html";
}

export function mostrarError(mens, campo) {
	let errorMessage = document.getElementById("errorMessage");
	//let errorContainer = document.getElementById("error");
	errorMessage.classList.add("error-message");
	errorMessage.classList.add("error-message");
	errorMessage.innerHTML = mens;
}

export function validaObligatorio(campo) {
	//var nomexpreg = /^([a-zA-Z\s-]{2,15})$/; //entre 2 y 15 carácteres
	return valor.trim() !== "";
}

export function validaSelect(selector) {
	let correcto = true;
	if (selector.value == -1) {
		mostrarError("Debe seleccionar una categoria", selector);
		correcto = false;
	}
	return correcto;
}

export function errorNoRegistro() {
	Swal.fire({
		icon: "error",
		title: "Oops...",
		text: "No puedes entrar sin Registrarte",
		showCancelButton: true,
		confirmButtonText: "Registrar",
		cancelButtonText: "Login"
	}).then((result) => {
		if (result.isConfirmed) {
			window.location.href = "registro.html";
		} else if (result.dismiss === Swal.DismissReason.cancel) {
			window.location.href = "login.html";
		}
	});
}

export function repetirContrasenya(campo1, campo2) {
	let correcto = true;
	if (campo1.value !== campo2.value) {
		campo2.value = "";
		mostrarError("Las contraseñas no coinciden", campo2);
		correcto = false;
	}
	return correcto;
}

export function errorSwal(titulo, mensaje) {
	Swal.fire({
		icon: "error",
		title: titulo,
		text: mensaje,
	})
}

export function errorSwalPag(titulo, mensaje, pagina) {
	Swal.fire({
		icon: "error",
		title: titulo,
		text: mensaje,
	}).then((result) => {
		if (result.isConfirmed) {
			window.location.href = pagina;
		}
	})
}

export function successSwal(titulo, mensaje) {
	Swal.fire({
		title: titulo,
		text: mensaje,
		icon: "success",
	}).then((result) => {
		if (result.isConfirmed) {
			document.querySelector("form").submit();
		}
	})
}

export function successSwalTimer(titulo, mensaje) {
	Swal.fire({
		position: "top",
		icon: "success",
		title: titulo,
		text: mensaje,
		width: 500,
		padding: "2em",

		showConfirmButton: false,
		timer: 1500,
		backdrop: `
    rgba(0, 0, 0, 0.5)
  `,
	}).then((result) => { document.querySelector("form").submit(); });
}

export function successSwalTimWeb(titulo, mensaje, web) {
	Swal.fire({
		position: "top",
		icon: "success",
		title: titulo,
		text: mensaje,
		width: 500,
		padding: "2em",

		showConfirmButton: false,
		timer: 1500,
		backdrop: `
    rgba(0, 0, 0, 0.5)
  `,
	}).then((result) => { window.location.href = web; });
}

export function errorUserNoLogged(mensaje) {
	Swal.fire({
		title: "Iniciar sesión",
		text: mensaje,
		icon: "warning",
		showCancelButton: true,
		confirmButtonText: "Iniciar sesión",
		cancelButtonText: "Cancelar",
	}).then((result) => {
		if (result.isConfirmed) {
			window.location.href = "/facefood/login";
		}
	});
}

export function precioDecimales(precio) {
	precio.addEventListener("blur", function () {
		let valor = parseFloat(this.value);
		if (!isNaN(valor)) {
			valor = valor.toFixed(2);
			this.value = valor;
		}
	});
}

export function aplicarClases(activo) {
	const menu = document.querySelectorAll(".a"); // Selecciona todos los elementos con clase "a"
	const iconos = document.querySelectorAll(".a i");

	menu.forEach(item => {
		if (item === activo) {
			item.classList.add("a-active");
			item.classList.remove("a-desabled");
		} else {
			item.classList.add("a-desabled");
			item.classList.remove("a-active");
		}
	});
	iconos.forEach(item => {
		if (item.parentElement === activo) {
			item.classList.add("selected");
			item.classList.remove("not-selected");
		} else {
			item.classList.add("not-selected");
			item.classList.remove("selected");
		}
	});
}

export function crearElemento(tipo = "br", padre, clases) {
	let elemento = document.createElement(tipo);

	if (padre != "") {
		if (Array.isArray(clases)) {
			for (let clase of clases) {
				elemento.classList.add(clase);
			}
		}

		padre.appendChild(elemento);
	}

	return elemento
}

export function validateSelect(select, errors, message) {
	if (select.value === "-1") {
		errors.push({ field: select, message: message });
		return false
	}
	return true
}

export function validateInput(input, errors, message) {
	if (input.value.trim() === "") {
		errors.push({ field: input, message: message });
		return false
	}
	return true
}

export function validateList(list, errors, message) {
	if (list.length === 0) {
		errors.push({ field: list, message: message });
		return false
	}
	return true
}

export function validateFile(input, contenedor, errors, message) {
	if (input.files.length === 0) {
		errors.push({ field: contenedor, message: message });
		return false
	}
	return true
}

export function validateEmail(input, error, message) {
	var emailexpreg = /[\w-\.]{3,}@([\w-]{2,}\.)*([\w-]{2,}\.)[\w-]{2,4}/;
	if (!emailexpreg.test(input.value)) {
		error.push({ field: input, message: message });
	}
	return true;
}

export function validateUsername(username, error) {
  const regex = /^[a-z0-9_]{3,16}$/;

  if (!regex.test(username.value)) {
    error.push({ field: username, message: "Solo minúsculas y números, mínimo 3 caracteres, máximo 16" });
  }
  return true;
}


function startSmoothScroll(contenedor, direction = 1) {
	const stepSize = 40;
	const interval = 250;

	const intervalId = setInterval(() => {
		contenedor.scrollBy({
			left: direction * stepSize,
			behavior: "smooth"
		});
	}, interval);

	return () => clearInterval(intervalId);
}

export function setupScrollZones() {
	const contenedor = document.getElementById("carousel-recetas");
	const leftZone = document.querySelector(".scroll-zone.left");
	const rightZone = document.querySelector(".scroll-zone.right");

	let stopScrollingLeft, stopScrollingRight;
	let hoverTimeout;

	leftZone.addEventListener("mouseenter", () => {
		hoverTimeout = setTimeout(() => {
			stopScrollingLeft = startSmoothScroll(contenedor, -1);
		}, 300);
	});
	leftZone.addEventListener("mouseleave", () => {
		clearTimeout(hoverTimeout);
		stopScrollingLeft?.();
	});

	rightZone.addEventListener("mouseenter", () => {
		hoverTimeout = setTimeout(() => {
			stopScrollingRight = startSmoothScroll(contenedor, 1);
		}, 300);
	});
	rightZone.addEventListener("mouseleave", () => {
		clearTimeout(hoverTimeout);
		stopScrollingRight?.();
	});

	const stopAllAutoScroll = () => {
		stopScrollingLeft?.();
		stopScrollingRight?.();
	};

	contenedor.addEventListener("wheel", stopAllAutoScroll);
	contenedor.addEventListener("mousedown", stopAllAutoScroll);
	contenedor.addEventListener("touchstart", stopAllAutoScroll);
}

export function setupArrowButtons() {
	const contenedor = document.getElementById("carousel-recetas");

	document.querySelector(".arrow.left").addEventListener("click", () => {
		contenedor.scrollBy({
			left: -300,
			behavior: "smooth"
		});
	});

	document.querySelector(".arrow.right").addEventListener("click", () => {
		contenedor.scrollBy({
			left: 300,
			behavior: "smooth"
		});
	});
}

export function toggleContraseña(itemHtml, input) {
	itemHtml.addEventListener('click', () => {
		const type = input.type === 'password' ? 'text' : 'password';
		input.type = type;
		itemHtml.textContent = type === 'password' ? '🙉' : '🙈';
	});
}

export function fillDisableInputForm(input, usuarioData) {
	document.getElementById(input).value = usuarioData;
	document.getElementById(input).setAttribute("disabled", "disabled");
}

export function iniciarLazyLoading() {
	const imgs = document.querySelectorAll('.lazy-img');

	if ('IntersectionObserver' in window) {
		const observer = new IntersectionObserver((entries, obs) => {
			entries.forEach(entry => {
				if (entry.isIntersecting) {
					const img = entry.target;
					img.src = img.dataset.src; // Set the src!
					img.classList.remove('lazy-img');
					observer.unobserve(img);
				}
			});
		}, {
			rootMargin: '200px', 
		});

		imgs.forEach(img => observer.observe(img));
	} else {
		imgs.forEach(img => img.src = img.dataset.src);
	}
}