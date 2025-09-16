export function renderizarFavorito(recetaSession) {
    const favIcon = document.createElement("img");
    favIcon.classList.add("favorite-icon"); 
    favIcon.src = recetaSession.favorita ? "img/icon/fav.svg" : "img/icon/emptyheart.svg";
    favIcon.setAttribute("data-receta-id", recetaSession.id);
    favIcon.setAttribute("data-favorita", recetaSession.favorita ? "true" : "false");
    favIcon.style.position = "absolute";
    favIcon.style.top = "10px";
    favIcon.style.right = "10px";
    favIcon.style.width = "40px";
    favIcon.style.cursor = "pointer";

    contenedorImagen.appendChild(favIcon);
}