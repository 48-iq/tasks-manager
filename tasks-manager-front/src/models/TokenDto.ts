import axios from 'axios'
import { host } from './host'

export type TokenDto = {
  access: string|null,
  refresh: string|null
}

export const tokenApi = {
    signIn: async (username: string, password: string) => {
    return axios.post<TokenDto>(`${host}/auth/sign-in`, {
      username,
      password
    }).then(res => res)
  },

  refresh: async (refresh: string) => {
    return axios.post<TokenDto>(`${host}/auth/refresh`, {
      refresh
    }).then(res => res)
  },

  signUp: async (username: string, password: string, roles: string[]) => {
    return axios.post<undefined>(`${host}/auth/sigh-up}`, {
      username,
      password,
      roles
    }).then(res => res)
  },

  changePassword: async (accountId: string, newPassword: string, refresh: string) => {
    return axios.post<undefined>(`${host}/auth/change-password/${accountId}`, {
      newPassword,
      refresh
    }).then(res => res)
  }
}


