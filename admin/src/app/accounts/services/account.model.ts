import { User } from "src/app/users/services/user.model";

export interface BankAccount {
    id: number;
    name: string;
    owner: User;
    iban?: string;
    balance: number;
}
