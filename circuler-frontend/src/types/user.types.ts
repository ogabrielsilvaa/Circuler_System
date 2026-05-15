export type UserStatus = 'APAGADO' | 'ATIVO' | 'INATIVO';

export type UserResponse = {
  id: number;
  name: string;
  email: string;
  cpf: string;
  status: UserStatus;
  roles: string[];
};
