import { User } from "@rest-client/model/user";

export interface BankAccount {
    id: number;
    name: string;
    owner: User;
    iban?: string;
    balance: number;
}
