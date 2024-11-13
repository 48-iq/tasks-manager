import type { RegisterFormData } from "./RegisterForm.vue";

export type Error {
  field: string,
  message: string,
}

export const validate = (data: RegisterFormData): Error[] => {
  const errors = []
  if (!data.login)
    errors.push({field: "", message: ""})
  return errors;
}