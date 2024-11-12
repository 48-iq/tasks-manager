import { tokenApi } from "@/models/TokenDto";
import { jwtDecode } from "jwt-decode";
import { defineStore } from "pinia";
import { ref } from "vue";
import { setCookie } from "typescript-cookie"

export type UserAuthData = {
  id: string
  username: string
  roles: string[]
  authorities: string[]
}

export type JwtPayload = {
  id: string
  username: string,
  roles: string[],
  authorities: string[],
  exp: number
}

export const useAuthStore = defineStore('auth', () => {
  const access = ref<string | null>(null)
  const refresh = ref<string | null>(null)
  const user = ref<UserAuthData | null>(null)
  const isLoading = ref<boolean>(false)
  const error = ref<string | null>(null)

  function signIn(username: string, password: string) {
    isLoading.value = true
    error.value = null
    user.value = null

    tokenApi.signIn(username, password)
      .then(res => {
        isLoading.value = false;
        access.value = res.data.access
        refresh.value = res.data.refresh
        if (access.value) {
          const decodedJwt = jwtDecode<JwtPayload>(access.value);
          user.value = {
            id: decodedJwt.id,
            username: decodedJwt.username,
            roles: decodedJwt.roles,
            authorities: decodedJwt.authorities
          }
          const exp = new Date(decodedJwt.exp * 1000)
          setTimeout(() => {
            if (refresh.value)
              refreshTokens(refresh.value)
          }, exp.getTime() - Date.now() - 1000 * 10)
          setCookie('refresh', refresh.value, { expires: exp, sameSite: 'lax', secure: true })
        }
      }).catch(err => {
        isLoading.value = false;
        error.value = err.message
        user.value = null
      })
  }

  function logout() {
    access.value = null
    refresh.value = null
  }

  function refreshTokens(r: string) {
    refresh.value = r
    if (refresh.value) {
      user.value = null
      const decodedJwt = jwtDecode<JwtPayload>(refresh.value);
      const exp = new Date(decodedJwt.exp * 1000).toUTCString()
      document.cookie = `refresh=${refresh.value} ; expires=${exp}`
      tokenApi.refresh(refresh.value)
        .then(res => {
          isLoading.value = false;
          access.value = res.data.access
          refresh.value = res.data.refresh
          if (access.value) {
            const decodedJwt = jwtDecode<JwtPayload>(access.value);
            user.value = {
              id: decodedJwt.id,
              username: decodedJwt.username,
              roles: decodedJwt.roles,
              authorities: decodedJwt.authorities
            }
          }
          const exp = new Date(decodedJwt.exp * 1000)
          setTimeout(() => {
            if (refresh.value)
              refreshTokens(refresh.value)
          }, exp.getTime() - Date.now() - 1000 * 10)
        }).catch(err => {
          isLoading.value = false;
          error.value = err.message
          user.value = null
        })
    }

  }



  return { access, refresh, user, isLoading, error, signIn, logout, refreshTokens }
})
