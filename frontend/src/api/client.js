import axios from 'axios'

// VITE_API_BASE_URL is the backend origin only; empty means same origin (Vite proxy).
const client = axios.create({
  baseURL: `${import.meta.env.VITE_API_BASE_URL ?? ''}/api/v1`,
})

export default client
