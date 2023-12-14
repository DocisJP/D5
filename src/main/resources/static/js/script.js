const calendar = document.querySelector(".calendar"),
  date = document.querySelector(".date"),
  daysContainer = document.querySelector(".days"),
  prev = document.querySelector(".prev"),
  next = document.querySelector(".next"),
  todayBtn = document.querySelector(".today-btn"),
  gotoBtn = document.querySelector(".goto-btn"),
  dateInput = document.querySelector(".date-input"),
  eventDay = document.querySelector(".event-day"),
  eventDate = document.querySelector(".event-date"),
  eventsContainer = document.querySelector(".events"),
  addEventBtn = document.querySelector(".add-event"),
  addEventWrapper = document.querySelector(".add-event-wrapper "),
  addEventCloseBtn = document.querySelector(".close "),
  addEventTitle = document.querySelector(".event-name "),
  addEventFrom = document.querySelector(".event-time-from "),
  addEventTo = document.querySelector(".event-time-to "),
  addEventSubmit = document.querySelector(".add-event-btn ");

let today = new Date();
let activeDay;
let month = today.getMonth();
let year = today.getFullYear();

const months = [
  "Enero",
  "Febrero",
  "Marzo",
  "Abril",
  "Mayo",
  "Junio",
  "Julio",
  "Agosto",
  "Septiembre",
  "Octubre",
  "Noviembre",
  "Diciembre",
];

//Agregamos un array de eventos por default
/*const eventsArr = [
  {
    day: 13,
    month: 11,
    year: 2023,
    events: [
      {
        title: "Event 1 lorem  sjf dsfds",
        time: "10:00 AM",
      },
      {
        title: "Event 2 lorem  sjf dsfds",
        time: "11:00 AM",
      },
    ],
  },
  {
    day: 10,
    month: 11,
    year: 2023,
    events: [
      {
        title: "Event 1 lorem  sjf dsfds",
        time: "10:00 AM",
      },
    ],
  },
];*/


const eventsArr = [
];

getEvents();
console.log(eventsArr);

function initCalendar() {
  //Para obtener los días del mes anterior y el mes actual todos los días y los días del mes siguiente REM
  const firstDay = new Date(year, month, 1);
  const lastDay = new Date(year, month + 1, 0);
  const prevLastDay = new Date(year, month, 0);
  const prevDays = prevLastDay.getDate();
  const lastDate = lastDay.getDate();
  const day = firstDay.getDay();
  const nextDays = 7 - lastDay.getDay() - 1;

  date.innerHTML = months[month] + " " + year;
  //agrego dias al dom
  let days = "";
  //Dias del mes anterior
  for (let x = day; x > 0; x--) {
    days += `<div class="day prev-date">${prevDays - x + 1}</div>`;
  }
  //Dias del mes actual
  for (let i = 1; i <= lastDate; i++) {
    //Compruebe si el evento está presente en el día actual
    let event = false;
    eventsArr.forEach((eventObj) => {
      if (
        eventObj.day === i &&
        eventObj.month === month + 1 &&
        eventObj.year === year
      ) {
        //Si el evento se encontro
        event = true;
      }
    });

    //Si el día es hoy, agregue la clase hoy
    if (
      i === new Date().getDate() &&
      year === new Date().getFullYear() &&
      month === new Date().getMonth()
    ) {
      activeDay = i;
      getActiveDay(i);
      updateEvents(i);
      //Si se encuentra el evento, agregue también la clase de evento
      if (event) {
        days += `<div class="day today active event">${i}</div>`;
      } else {
        days += `<div class="day today active" >${i}</div>`;
      }
    } else {
      //Agregue el resto tal como está
      if (event) {
        days += `<div class="day event" >${i}</div>`;
      } else {
        days += `<div class="day " >${i}</div>`;
      }
    }
  }
  //Días del próximo mes
  for (let j = 1; j <= nextDays; j++) {
    days += `<div class="day next-date">${j}</div>`;
  }

  daysContainer.innerHTML = days;
  //Agregar lista después de la inicialización del calendario
  addListener();
}

//siguiente mes
function prevMonth() {
  month--;
  if (month < 0) {
    month = 11;
    year--;
  }
  initCalendar();
}
//mes anterior
function nextMonth() {
  month++;
  if (month > 11) {
    month = 0;
    year++;
  }
  initCalendar();
}
prev.addEventListener("click", prevMonth);
next.addEventListener("click", nextMonth);

initCalendar();

//Vamos a crear una función para agregar un días después
function addListener() {
  const days = document.querySelectorAll(".day");
  days.forEach((day) => {
    day.addEventListener("click", (e) => {
      //Llamada activa al día siguiente del clic
      getActiveDay(e.target.innerHTML);
      updateEvents(Number(e.target.innerHTML));
      //Establecer el día actual como día activo
      activeDay = Number(e.target.innerHTML);
      //Eliminar activo del día ya activo
      days.forEach((day) => {
        day.classList.remove("active");
      });

      //Si el día del mes anterior hizo clic en Ir al mes anterior y agregar activo
      if (e.target.classList.contains("prev-date")) {
        prevMonth();

        setTimeout(() => {
          //selecciona todos los dias de ese mes
          const days = document.querySelectorAll(".day");
          //Después de ir al mes anterior, agregue activo a clicado
          days.forEach((day) => {
            if (
              !day.classList.contains("prev-date") &&
              day.innerHTML === e.target.innerHTML
            ) {
              day.classList.add("active");
            }
          });
        }, 100);
      } else if (e.target.classList.contains("next-date")) {
        nextMonth();

        setTimeout(() => {
          //selecciona todos los dias de ese mes
          const days = document.querySelectorAll(".day");
          //Después de ir al mes anterior, agregue activo a clicado
          days.forEach((day) => {
            if (
              !day.classList.contains("next-date") &&
              day.innerHTML === e.target.innerHTML
            ) {
              day.classList.add("active");
            }
          });
        }, 100);
      } else {
        e.target.classList.add("active");
      }
    });
  });
}

//Nuestro calendario esta lista
//Agregamos funcionalidad a
todayBtn.addEventListener("click", () => {
  today = new Date();
  month = today.getMonth();
  year = today.getFullYear();
  initCalendar();
});

dateInput.addEventListener("input", (e) => {
  //Permitir solo número Eliminar cualquier otra cosa
  dateInput.value = dateInput.value.replace(/[^0-9/]/g, "");
  if (dateInput.value.length === 2) {
    //Agregue una barra oblicua si ingresó dos números
    dateInput.value += "/";
  }

  if (dateInput.value.length > 7) {
    //No permitir más de 7 caracteres
    dateInput.value = dateInput.value.slice(0, 7);
  }
  //si se presiona la tecla de retroceso
  if (e.inputType === "deleteContentBackward") {
    if (dateInput.value.length === 3) {
      dateInput.value = dateInput.value.slice(0, 2);
    }
  }
});

gotoBtn.addEventListener("click", gotoDate);

function gotoDate() {
  console.log("here");
  const dateArr = dateInput.value.split("/");
  if (dateArr.length === 2) {
    if (dateArr[0] > 0 && dateArr[0] < 13 && dateArr[1].length === 4) {
      month = dateArr[0] - 1;
      year = dateArr[1];
      initCalendar();
      return;
    }
  }
  alert("Dato invalido");
}

function getActiveDay(date) {
  const day = new Date(year, month, date);
  const options = {
    weekday: "long",
    year: "numeric",
    month: "long",
    day: "numeric",
  };
  const locale = "es-ES";

  const formattedDate = day.toLocaleDateString(locale, options);
  const [dayName, eventDateStr] = formattedDate.split(", ");

  eventDay.innerHTML = dayName;
  eventDate.innerHTML = eventDateStr;
}

getActiveDay(date);

//función para mostrar el evento de ese día
function updateEvents(date) {
  let events = "";
  eventsArr.forEach((event) => {
    //Obtener evento solo del dia activo
    if (
      date === event.day &&
      month + 1 === event.month &&
      year === event.year
    ) {
      //A continuación, mostrar evento en el documento
      event.events.forEach((event) => {
        events += `<div class="event">
          <div class="title">
            <i class="fas fa-circle"></i>
            <h3 class="event-title">${event.title}</h3>
          </div>
          <div class="event-time">
            <span class="event-time">${event.time}</span>
          </div>
      </div>`;
      });
    }
  });

  //si no se encuentra nada
  if (events === "") {
    events = `<div class="no-event">
    <h3>No Hay Eventos</h3>
    </div>`;
  }

  eventsContainer.innerHTML = events;
  saveEvents();
}

addEventBtn.addEventListener("click", () => {
  addEventWrapper.classList.toggle("active");
});
addEventCloseBtn.addEventListener("click", () => {
  addEventWrapper.classList.remove("active");
});

document.addEventListener("click", (e) => {
  if (e.target != addEventBtn && !addEventWrapper.contains(e.target)) {
    addEventWrapper.classList.remove("active");
  }
});

//Permitir solo 50 caracteres en el título
addEventTitle.addEventListener("input", (e) => {
  addEventTitle.value = addEventTitle.value.slice(0, 50);
});

//Formato de hora en tiempo de origen y destino
addEventFrom.addEventListener("input", (e) => {
  //Eliminar cualquier otro número
  addEventFrom.value = addEventFrom.value.replace(/[^0-9:]/g, "");
  //Si se introdujeron dos números, agrega automáticamente :
  if (addEventFrom.value.length === 2) {
    addEventFrom.value += ":";
  }
  //No permitas que el usuario ingrese más de 5 caracteres
  if (addEventFrom.value.length > 5) {
    addEventFrom.value = addEventFrom.value.slice(0, 5);
  }
});

//Hacemos lo mismo con el tiempo
addEventTo.addEventListener("input", (e) => {
  //Eliminar cualquier otro número
  addEventTo.value = addEventTo.value.replace(/[^0-9:]/g, "");
  //Si se introdujeron dos números, agrega automáticamente :
  if (addEventTo.value.length === 2) {
    addEventTo.value += ":";
  }
  //No permitas que el usuario ingrese más de 5 caracteres
  if (addEventTo.value.length > 5) {
    addEventTo.value = addEventTo.value.slice(0, 5);
  }
});

//Vamos a crear una función para agregar eventos
addEventSubmit.addEventListener("click", () => {
  const eventTitle = addEventTitle.value;
  const eventTimeFrom = addEventFrom.value;
  const eventTimeTo = addEventTo.value;

  //Validamos
  if (eventTitle === "" || eventTimeFrom === "" || eventTimeTo === "") {
    alert("Los campos no pueden estar vacios");
    return;
  }

  const timeFromArr = eventTimeFrom.split(":");
  const timeToArr = eventTimeTo.split(":");

  if (
    timeFromArr.length != 2 ||
    timeToArr.length != 2 ||
    timeFromArr[0] > 23 ||
    timeFromArr[1] > 59 ||
    timeToArr[0] > 23 ||
    timeToArr[1] > 59
  ) {
    alert("Formato de Hora inválido");
    return;
  }

  const timeFrom = convertTime(eventTimeFrom);
  const timeTo = convertTime(eventTimeTo);

  let eventExist = false;

  //Compruebe si el día actual tiene algún evento y luego agregue
  eventsArr.forEach((item) => {
    if (
      item.day === activeDay &&
      item.month === month + 1 &&
      item.year === year
    ) {
      item.events.forEach((event) => {
        if (event.title === eventTitle) {
          eventExist = true;
        }
      });
    }
  });
  if (eventExist) {
    alert("Ya hay un evento ese dia y en ese horario");
    return;
  }

  const newEvent = {
    title: eventTitle,
    time: timeFrom + " - " + timeTo,
  };
  console.log(newEvent);
  console.log(activeDay);
  let eventAdded = false;
  if (eventsArr.length > 0) {
    eventsArr.forEach((item) => {
      if (
        item.day === activeDay &&
        item.month === month + 1 &&
        item.year === year
      ) {
        item.events.push(newEvent);
        eventAdded = true;
      }
    });
  }

  //
  if (!eventAdded) {
    eventsArr.push({
      day: activeDay,
      month: month + 1,
      year: year,
      events: [newEvent],
    });
  }

  // Eliminar la clase activa Agregar evento
  addEventWrapper.classList.remove("active");
  //Borrar los campos
  addEventTitle.value = "";
  addEventFrom.value = "";
  addEventTo.value = "";

  //Mostrar evento agregado
  updateEvents(activeDay);

  //Seleccione Día activo y agregue clase de evento si no se agrega
  const activeDayEl = document.querySelector(".day.active");
  if (!activeDayEl.classList.contains("event")) {
    activeDayEl.classList.add("event");
  }
});

//Función para eliminar el evento cuando se hace clic en el evento
eventsContainer.addEventListener("click", (e) => {
  if (e.target.classList.contains("event")) {
    if (confirm("Desea Eliminar esta Reunión")) {
      const eventTitle = e.target.children[0].children[1].innerHTML;
      eventsArr.forEach((event) => {
        if (
          event.day === activeDay &&
          event.month === month + 1 &&
          event.year === year
        ) {
          event.events.forEach((item, index) => {
            if (item.title === eventTitle) {
              event.events.splice(index, 1);
            }
          });

          //remueve el dia si no hay eventos del eventsArr
          if (event.events.length === 0) {
            eventsArr.splice(eventsArr.indexOf(event), 1);
            //remueve el event class de day

            const activeDayEl = document.querySelector(".day.active");
            if (activeDayEl.classList.contains("event")) {
              activeDayEl.classList.remove("event");
            }
          }
        }
      });
      updateEvents(activeDay);
    }
  }
});

//Función para guardar eventos en el almacenamiento local
function saveEvents() {
  localStorage.setItem("events", JSON.stringify(eventsArr));
}

//Función para obtener eventos del almacenamiento local
function getEvents() {
  //Compruebe si los eventos ya están guardados en el almacenamiento local y, 
  //a continuación, devuelva el evento, de lo contrario, null
  if (localStorage.getItem("events") === null) {
    return;
  }
  eventsArr.push(...JSON.parse(localStorage.getItem("events")));
}

function convertTime(time) {
  //Convertir la hora a formato de 24 horas
  let timeArr = time.split(":");
  let timeHour = timeArr[0];
  let timeMin = timeArr[1];
  let timeFormat = timeHour >= 12 ? "PM" : "AM";
  timeHour = timeHour % 12 || 12;
  time = timeHour + ":" + timeMin + " " + timeFormat;
  return time;
}

