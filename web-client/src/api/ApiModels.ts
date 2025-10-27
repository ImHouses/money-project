// Response payloads
export interface BudgetResponse {
  year: number;
  month: number;
  items: Array<BudgetItemResponse>;
}

export interface BudgetItemResponse {
  id: number;
  name: string;
  amount: string;
  remainingAmount: string;
  transactions: Array<TransactionResponse>;
}

export interface TransactionResponse {
  id: number;
  day: number;
  month: number;
  year: number;
  amount: string;
  budgetItemId: number;
  note?: string;
}

// Request payloads
export interface TransactionPayload {
  amount: string;
  budgetItemId: number;
  day: number;
  note?: string;
}
