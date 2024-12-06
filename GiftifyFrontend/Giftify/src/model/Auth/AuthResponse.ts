
export class AuthResponse {
  constructor(public accessToken: string,
              public refreshToken: string,
              public message: string) {
  }
}
