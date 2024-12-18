
export class AuthResponse {
  constructor(public access_token: string,
              public refresh_token: string,
              public message: string) {
  }
}
