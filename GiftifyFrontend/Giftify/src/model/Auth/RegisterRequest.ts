export class RegisterRequest {
  constructor(
    public email: string,
    public password: string,
    public username: string
  ) {}
}
