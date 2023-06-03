import axios from "axios";

import {TOKEN} from "./util/constants"

const axiosIns = axios.create({
  baseURL: "http://localhost:8080",
});

axiosIns.interceptors.request.use(
    (config) => {
      const token = localStorage.getItem(TOKEN);

      if (token) {
        config.headers.Authorization = `Bearer ${token}`;
      }

      return config;
    },
    (error) => {
      console.log("from axiosInstance.js: ", error)
      return Promise.reject(error)
    }
);

axiosIns.interceptors.response.use((config) => config, (error) => {
  if (error.response.status === 401) {
    localStorage.removeItem(TOKEN);
  }

  console.dir("ERROR: ", error.response)

  return Promise.reject(error)
})

export default axiosIns;
