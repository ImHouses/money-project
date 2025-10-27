import axios from 'axios';
import type { BudgetResponse, TransactionPayload } from './ApiModels';

class Api {
  private apiUrl = 'localhost:8080/api/v1/';
  private httpClient = axios.create({
    baseURL: this.apiUrl,
  });
  async getBudget(month: number, year: number): Promise<BudgetResponse> {
    return await this.httpClient.get(`budget/${year}/${month}`);
  }

  async createTransaction(
    transactionData: TransactionPayload,
    month: number,
    year: number
  ) {
    const payload = {
      amount: transactionData.amount,
      budget_item_id: transactionData.budgetItemId,
      day: transactionData.day,
      note: transactionData.note,
    };
    return await this.httpClient.post(`budget/${year}/${month}`, payload);
  }
}

const api = new Api();

export default api;
