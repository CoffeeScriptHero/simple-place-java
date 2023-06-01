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
    (error) => Promise.reject(error)
);

export default axiosIns;