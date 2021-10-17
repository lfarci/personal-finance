import { Owner } from "src/app/owners/services/owner.model";

export interface BankAccount {
    id: number;
    name: string;
    owner: Owner;
    iban?: string;
    balance: number;
}