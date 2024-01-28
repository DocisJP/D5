//Seccion Menu
function showSection(sectionId) {
// Oculta todas las secciones
    var sections = document.querySelectorAll(".content-section");
    sections.forEach(function (section) {
        section.classList.remove("active-section");
    });
    // Muestra la sección específica según el enlace clicado
    var selectedSection = document.getElementById(sectionId + "Section");
    if (selectedSection) {
        selectedSection.classList.add("active-section");
    }
}


//Proyecto 
// Función para mostrar sugerencias
function mostrarSugerencias(valor) {
    console.log('Valor de búsqueda:', valor);

    // Oculta la lista de sugerencias si el valor está vacío
    if (!valor.trim()) {
        document.getElementById('sugerenciasProyectos').style.display = 'none';
        limpiarVista();
        return;
    }

    // Limpia la lista de sugerencias
    var sugerenciasProyectos = document.getElementById('sugerenciasProyectos');
    sugerenciasProyectos.innerHTML = '';

    // Realiza la solicitud al controlador para obtener las sugerencias
    fetch('/admin/sugerirNombresProyectos?query=' + encodeURIComponent(valor), {
        method: 'GET', // Cambia el método a GET
    })
            .then(response => response.json())
            .then(data => {
                console.log('Datos recibidos:', data);

                // Agrega las sugerencias a la lista
                var ul = document.createElement('ul');

                // Verifica si 'data' es una cadena en lugar de una matriz
                var sugerencias = Array.isArray(data) ? data : [data];

                sugerencias.forEach(function (sugerencia) {
                    var li = document.createElement('li');
                    li.textContent = sugerencia;
                    li.onclick = function () {
                        document.getElementById('nombreProyecto').value = sugerencia;
                        sugerenciasProyectos.style.display = 'none';
                        buscarProyecto();
                    };
                    ul.appendChild(li);
                });

                sugerenciasProyectos.appendChild(ul);

                // Muestra el div si hay sugerencias, ocúltalo de lo contrario
                sugerenciasProyectos.style.display = sugerencias.length > 0 ? 'block' : 'none';
            })
            .catch(error => console.error('Error al obtener sugerencias:', error));
}

// Función para manejar la selección de sugerencias
function seleccionarSugerencia(event) {
    var sugerenciaSeleccionada = event.target.textContent;
    document.getElementById('nombreProyecto').value = sugerenciaSeleccionada;
    document.getElementById('sugerenciasProyectos').style.display = 'none';
}

// Función para realizar la búsqueda y actualizar la vista
function buscarProyecto() {
    var valor = document.getElementById('nombreProyecto').value;
    var data = {nombreProyecto: valor};

    limpiarVista();

    fetch('/admin/buscar', {
        method: 'POST', // Cambia a POST
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
            .then(response => response.json())
            .then(data => {

                // Empresas
                var empresasContainer = document.getElementById('empresasContainer');
                if (data.empresas && data.empresas.length > 0) {
                    var empresasList = document.createElement('ul');
                    data.empresas.forEach(function (empresa) {
                        var li = document.createElement('li');
                        li.textContent = empresa;
                        empresasList.appendChild(li);
                    });
                    // Crear el título y agregarlo al contenedor
                    var empresasTitle = document.createElement('h2');
                    empresasTitle.textContent = 'Empresas Asociadas';
                    empresasContainer.appendChild(empresasTitle);
                    empresasContainer.appendChild(empresasList);
                } else {
                    empresasContainer.innerHTML = '<div class="alert alert-info" style="border: 2px solid var(--fondo)">No se encontraron empresas asociadas al proyecto.</div>';
                }

                // Usuarios
                var usuariosContainer = document.getElementById('usuariosContainer');
                if (data.usuarios && data.usuarios.length > 0) {
                    var usuariosList = document.createElement('ul');
                    data.usuarios.forEach(function (usuario) {
                        var li = document.createElement('li');
                        li.textContent = usuario.toString();
                        usuariosList.appendChild(li);
                    });
                    // Crear el título y agregarlo al contenedor
                    var usuariosTitle = document.createElement('h2');
                    usuariosTitle.textContent = 'Usuarios Asociados';
                    usuariosContainer.appendChild(usuariosTitle);

                    usuariosContainer.appendChild(usuariosList);
                } else {
                    usuariosContainer.innerHTML = '<div class="alert alert-info" style="border: 2px solid var(--fondo)">No se encontraron usuarios asociados al proyecto.</div>';
                }
                // Reuniones
                // Obtener el contenedor de reuniones en el DOM
                var reunionesContainer = document.getElementById('reunionesContainer');

                // Verificar si se encontraron reuniones
                if (data.reuniones !== null && data.reuniones.length > 0) {
                    // Crear una lista no ordenada para mostrar las reuniones
                    var reunionesList = document.createElement('ul');

                    // Iterar sobre cada reunión y agregarla a la lista
                    data.reuniones.forEach(function (reunion) {
                        // Crear un elemento de lista para cada reunión
                        var reunionItem = document.createElement('li');

                        // Asignar el texto para mostrar la información de la reunión
                        reunionItem.textContent = reunion;

                        // Agregar el elemento de lista a la lista de reuniones
                        reunionesList.appendChild(reunionItem);
                    });

                    // Crear el título y agregarlo al contenedor
                    var reunionesTitle = document.createElement('h2');
                    reunionesTitle.textContent = 'Reuniones Asociadas';
                    reunionesContainer.appendChild(reunionesTitle);
                    // Agregar la lista de reuniones al contenedor de reuniones en el DOM
                    reunionesContainer.appendChild(reunionesList);
                } else {
                    // Si no se encontraron reuniones, mostrar un mensaje de alerta
                    reunionesContainer.innerHTML = '<div class="alert alert-info" style="border: 2px solid var(--fondo)">No se encontraron reuniones asociadas al proyecto.</div>';
                }


                // Tareas
                var tareasContainer = document.getElementById('tareasContainer');
                if (data.tareas !== '' && data.tareas.length > 0) {
                    var tareasList = document.createElement('ul');
                    data.tareas.forEach(function (tarea) {
                        var li = document.createElement('li');
                        li.textContent = tarea;
                        tareasList.appendChild(li);
                    });
                    // Crear el título y agregarlo al contenedor
                    var tareasTitle = document.createElement('h2');
                    tareasTitle.textContent = 'Tareas Asociadas';
                    tareasContainer.appendChild(tareasTitle);
                    tareasContainer.appendChild(tareasList);
                } else {
                    tareasContainer.innerHTML = '<div class="alert alert-info" style="border: 2px solid var(--fondo)">No se encontraron tareas del proyecto.</div>';
                }

            })
            .catch(error => {
                console.error('Error en la solicitud:', error);
                // Manejar el error, mostrar un mensaje al usuario, etc.
            });
}

// Función para limpiar la vista
function limpiarVista() {
    var empresasContainer = document.getElementById('empresasContainer');
    empresasContainer.innerHTML = '';

    var usuariosContainer = document.getElementById('usuariosContainer');
    usuariosContainer.innerHTML = '';

    var reunionesContainer = document.getElementById('reunionesContainer');
    reunionesContainer.innerHTML = '';

    var tareasContainer = document.getElementById('tareasContainer');
    tareasContainer.innerHTML = '';

}


// Selecciona el formulario y asocia la función buscarEmpresa al evento submit
document.getElementById('formBusquedaProyecto').addEventListener('submit', function (event) {
    // Evita que el formulario se envíe normalmente (ya que queremos manejarlo con JavaScript)
    event.preventDefault();
    // Llama a la función buscarEmpresa al hacer clic en el botón
    buscarProyecto();

});

// Oculta la lista de sugerencias al cargar la página
document.addEventListener('DOMContentLoaded', function () {
    document.getElementById('sugerenciasProyectos').style.display = 'none';
});

//Seccion Usuarios
function showDetails(userId) {
    console.log("Mostrando detalles para el usuario con ID:", userId);
    var postData = {
        userId: userId
    };
    fetch('/admin/dashboard', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(postData)
    })
            .then(response => {
                console.log('Respuesta del servidor:', response);
                return response.json();
            })
            .then(data => {
                console.log('Datos recibidos:', data);
                console.log('Respuesta del servidor:', data);
                var userDetailsSection = document.getElementById('detailsSection-' + userId);
                if (userDetailsSection) {
                    userDetailsSection.style.display = "block";
                    // Actualizar la vista con los datos recibidos
                    userDetailsSection.innerHTML = `
                        <h4>Proyectos</h4>
                        <p>Proyectos Totales: ${data.proyectosTotales}</p>
                        <p>Proyectos Pendientes: ${data.proyectosEnProgreso}</p>
                        <p>Proyectos Finalizados: ${data.proyectosFinalizados}</p>
                        <h4>Reuniones</h4>
                        <p>Reuniones Totales: ${data.reunionesTotales}</p>
                        <p>Reuniones Pendientes: ${data.reunionesEnProgreso}</p>
                        <p>Reuniones Finalizados: ${data.reunionesFinalizados}</p>
                        <h4>Tareas</h4>
                        <p>Tareas Totales: ${data.tareasTotales}</p>
                        <p>Tareas Pendientes: ${data.tareasEnProgreso}</p>
                        <p>Tareas Finalizados: ${data.tareasFinalizados}</p>
                        <button onclick="hideDetails()">Ver Menos</button>
                    `;
                    userDetailsSection.scrollIntoView({behavior: 'smooth'});
                } else {
                    console.error("No se encontró la sección de detalles para el usuario con ID:", userId);
                }
            })
            .catch(error => {
                console.error('Error en la solicitud POST:', error);
            });
}

function hideDetails() {
    var detailsSections = document.querySelectorAll(".detailsSection");
    detailsSections.forEach(function (section) {
        section.style.display = "none";
    });
}

//Seccion Empresa
/// Función para mostrar sugerencias
function mostrarSugerenciasEmpresa(valor) {
    console.log('Valor de búsqueda:', valor);

    // Oculta la lista de sugerencias si el valor está vacío
    if (!valor.trim()) {
        document.getElementById('sugerenciasEmpresas').style.display = 'none';
        limpiarVistaEmpresa(); // Limpiar la vista al mostrar sugerencias
        return;
    }

    // Limpia la lista de sugerencias
    var sugerenciasEmpresas = document.getElementById('sugerenciasEmpresas');
    sugerenciasEmpresas.innerHTML = '';

    // Realiza la solicitud al controlador para obtener las sugerencias
    fetch('/admin/sugerirNombresEmpresas?query=' + encodeURIComponent(valor), {
        method: 'GET', // Cambia el método a GET
    })
            .then(response => response.json())
            .then(data => {
                console.log('Datos recibidos:', data);

                // Agrega las sugerencias a la lista
                var ul = document.createElement('ul');

                // Verifica si 'data' es una cadena en lugar de una matriz
                var sugerenciasEmp = Array.isArray(data) ? data : [data];

                sugerenciasEmp.forEach(function (sugerencia) {
                    var li = document.createElement('li');
                    li.textContent = sugerencia;
                    li.onclick = function () {
                        document.getElementById('nombreEmpresa').value = sugerencia;
                        sugerenciasEmpresas.style.display = 'none';
                        buscarEmpresa(); // Realizar búsqueda al seleccionar sugerencia
                    };
                    ul.appendChild(li);
                });

                sugerenciasEmpresas.appendChild(ul);

                // Muestra el div si hay sugerencias, ocúltalo de lo contrario
                sugerenciasEmpresas.style.display = sugerenciasEmp.length > 0 ? 'block' : 'none';
            })
            .catch(error => console.error('Error al obtener sugerencias:', error));
}

// Función para realizar la búsqueda y actualizar la vista
function buscarEmpresa() {
    var valor = document.getElementById('nombreEmpresa').value;
    var data = {nombreEmpresa: valor};

    // Limpiar la vista antes de actualizarla
    limpiarVistaEmpresa();

    fetch('/admin/buscarUsuarioYProyectos', {
        method: 'POST', // Cambia a POST
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
            .then(response => response.json())
            .then(data => {
                // Proyectos
                var proyectosAsociadosContainer = document.getElementById('proyectosAsociadosContainer');
                if (data.proyectos && data.proyectos.length > 0) {
                    var proyectosList = document.createElement('ul');
                    data.proyectos.forEach(function (proyecto) {
                        var li = document.createElement('li');
                        li.textContent = proyecto;
                        proyectosList.appendChild(li);
                    });
                    // Crear el título y agregarlo al contenedor
                    var proyectosTitle = document.createElement('h2');
                    proyectosTitle.textContent = 'Proyectos Asociados';
                    proyectosAsociadosContainer.appendChild(proyectosTitle);
                    proyectosAsociadosContainer.appendChild(proyectosList);
                } else {
                    proyectosAsociadosContainer.innerHTML = '<div class="alert alert-info" style="border: 2px solid var(--fondo)">No se encontraron empresas asociadas al proyecto.</div>';
                }

                // Usuarios
                var usuariosContainerEmpresa = document.getElementById('usuariosContainerEmpresa');
                if (data.usuarios && data.usuarios.length > 0) {
                    var usuariosList = document.createElement('ul');
                    data.usuarios.forEach(function (usuario) {
                        var li = document.createElement('li');
                        li.textContent = usuario.toString();
                        usuariosList.appendChild(li);
                    });
                    // Crear el título y agregarlo al contenedor
                    var usuariosTitle = document.createElement('h2');
                    usuariosTitle.textContent = 'Usuarios Asociados';
                    usuariosContainerEmpresa.appendChild(usuariosTitle);
                    usuariosContainerEmpresa.appendChild(usuariosList);
                } else {
                    usuariosContainerEmpresa.innerHTML = '<div class="alert alert-info" style="border: 2px solid var(--fondo)">No se encontraron usuarios asociados al proyecto.</div>';
                }
            })
            .catch(error => {
                console.error('Error en la solicitud:', error);
                // Manejar el error, mostrar un mensaje al usuario, etc.
            });
}

// Función para limpiar la vista
function limpiarVistaEmpresa() {
    var usuariosContainerEmpresa = document.getElementById('usuariosContainerEmpresa');
    usuariosContainerEmpresa.innerHTML = '';

    var proyectosAsociadosContainer = document.getElementById('proyectosAsociadosContainer');
    proyectosAsociadosContainer.innerHTML = '';
}

// Selecciona el formulario y asocia la función buscarEmpresa al evento submit
document.getElementById('formBusquedaEmpresa').addEventListener('submit', function (event) {
    // Evita que el formulario se envíe normalmente (ya que queremos manejarlo con JavaScript)
    event.preventDefault();

    // Llama a la función buscarEmpresa al hacer clic en el botón
    buscarEmpresa();
});

// Oculta la lista de sugerencias al cargar la página
document.addEventListener('DOMContentLoaded', function () {
    document.getElementById('sugerenciasEmpresas').style.display = 'none';
});


////Seccion Dashboard
//
// Configuración del gráfico de carga de trabajo de los agentes
var listaAgentes = [];
var workloadData = {
    labels: listaAgentes,
    datasets: [
        {
            label: "Carga de Trabajo de Agentes",
            data: [],
            backgroundColor: "rgba(75, 192, 192)",
            borderColor: "rgba(75, 192, 192)",
            borderWidth: 1
        }
    ]
};

// Obtener los datos a visualizar
fetch('/admin/devolverAgentes',
        {
            method: 'GET',
        }
)
        .then(response => response.text())
        .then(data => {
            const temp1 = JSON.parse(data)
            let elemLabels = Object.entries(temp1)
            elemLabels.forEach(function (elem, i) {
                listaAgentes.push(Object.keys(elem[1]));
                workloadData.datasets[0].data.push(Object.values(elem[1]));
            })
        })
        .catch(e => {
            listaAgentes.push("No hay: " + e)
            workloadData.labels = listaAgentes;
            workloadData.datasets[0].data.push("1");
        });

var cadenaGenerica = document.getElementById('avblAgents').textContent + " Agentes Disponibles";

var workloadOptions = {
    scales: {
        y: {
            beginAtZero: true
        }
    }
};

var workloadChart = new Chart(document.getElementById("workloadChart"), {
    type: "bar",
    data: workloadData,
    options: workloadOptions
});

// Configuración del gráfico de estado de los proyectos
var projectStatusData = {
    labels: ["En Progreso", "Completado"],
    datasets: [
        {
            data: [
                document.getElementById('inProgressProjects').textContent,
                document.getElementById('completedProjects').textContent],
            backgroundColor: [
                "rgba(255, 205, 86)",
                "rgba(54, 162, 235)"
            ],
            borderColor: [
                "rgba(255, 205, 86)",
                "rgba(54, 162, 235)"
            ],
            borderWidth: 1
        }
    ]
};

var projectStatusOptions = {
    responsive: true,
    plugins: {
        legend: {
            position: "top"
        },
        title: {
            display: true,
            text: "Estado de los Proyectos"
        }
    }
};

var projectStatusChart = new Chart(
        document.getElementById("projectStatusChart"),
        {
            type: "pie",
            data: projectStatusData,
            options: projectStatusOptions
        }
);
  