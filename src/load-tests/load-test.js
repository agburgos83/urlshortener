import http from 'k6/http';

export const options = {
  scenarios: {
    constant_request_rate: {
      executor: 'constant-arrival-rate',
      rate: 83, // Solicitudes por segundo (5000 RPM)
      timeUnit: '1s', // Unidad de tiempo
      duration: '4m', // Duración total del test
      preAllocatedVUs: 100, // Usuarios necesarios para sostener la carga
      maxVUs: 5000, // Límite máximo de usuarios
    },
  },
};

export default function () {
  const url = 'http://localhost:8080/redirect/e09a230c';

  // Solicitud GET a la URL corta
  http.get(url);
}