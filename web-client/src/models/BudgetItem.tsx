export interface BudgetItem {
  id: number;
  name: string;
  amount: string;
  remainingAmount: string;
  transactionsNumber: number;
  description?: string;
}
