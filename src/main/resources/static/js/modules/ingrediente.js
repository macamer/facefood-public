import { findAllIngredientes } from "./api.js"
import { crearElemento } from "./ui.js"

export function listIngredientes(idIng, nombre, cantMedida, ingcontainer) {

    let colDiv = crearElemento("div", ingcontainer, ["g-col-3"])
    let cantlist = crearElemento("ul", colDiv)
    cantlist.innerHTML = `<li class="bold">${cantMedida}</li>`

    let inglist = crearElemento("ul", colDiv);
    inglist.innerHTML = `<li>${nombre}</li>`;

    let ingSesion = JSON.parse(sessionStorage.getItem("listIng")) || [];
    let existe = ingSesion.some(([id]) => id === (idIng || nombre));
    if (!existe) {
        ingSesion.push([idIng || nombre, cantMedida]);
        sessionStorage.setItem("listIng", JSON.stringify(ingSesion));
    }

    // Botón eliminar
    let btnDelete = crearElemento("i", colDiv, ["bi", "bi-trash3"]);
    btnDelete.addEventListener("click", function () {
        ingcontainer.removeChild(colDiv);
        let ingSesionActual = JSON.parse(sessionStorage.getItem("listIng")) || [];
        let index = ingSesionActual.findIndex(([n]) => n === (idIng || nombre));
        if (index !== -1) {
            ingSesionActual.splice(index, 1);
            sessionStorage.setItem("listIng", JSON.stringify(ingSesionActual));
        }
    });
}

export async function showAllIngredientes(ingSelect) {
    try {
        const ing = await findAllIngredientes();

        // Limpiamos el select y agregamos la opción por defecto
        ingSelect.innerHTML = '<option value="-1" disabled selected>Selecciona un Ingrediente</option>';

        // Llenamos el select con los países obtenidos
        ing.forEach(i => {
            const option = document.createElement("option");
            option.value = i.id;
            option.textContent = i.nombre;
            ingSelect.appendChild(option);
        });

    } catch (error) {
        console.error("Error al obtener los ingredientes", error);
    }
}

export function fillIngredientes(ingredientes) {
	const ingcontainer = document.getElementById("list-ing");
	let ingSesion = [];

	ingredientes.forEach(ing => {
		const idIng = ing.ingredienteDTO.id;
		const nombreIng = ing.ingredienteDTO.nombre;
		const cantidad = `${ing.cantidad}`;
		listIngredientes(idIng, nombreIng, cantidad, ingcontainer);
		ingSesion.push([idIng, cantidad]);
	});

	sessionStorage.setItem("listIng", JSON.stringify(ingSesion));
}

// --- Función extra para mostrar "No hay ingredientes"
function mostrarMensajeNoIngredientes(container) {
    const mensaje = document.createElement("div");
    mensaje.id = "no-ingredientes-msg";
    mensaje.classList.add("no-ingredientes", "smll", "mt-2");
    mensaje.textContent = "🥣 No hay ingredientes añadidos.";
    container.appendChild(mensaje);
}