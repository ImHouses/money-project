import type { BudgetItem } from '../../models/BudgetItem';
import type { RecordTransactionProps } from './RecordTransactionProps';
import './RecordTransaction.css';
import { useState } from 'react';

interface State {
  amountText: string;
  amount: number;
  day?: number;
  budgetItemId?: number;
  note: string;
}

const RecordTransaction = ({ month, year }: RecordTransactionProps) => {
  function transformAmount(amount: string): number {
    let hasDecimalPoint = false;
    const cleanAmountCharacters = amount.split('').filter((character) => {
      if (character === '.' && !hasDecimalPoint) {
        hasDecimalPoint = true;
        return true;
      }
      return /\d/.test(character);
    });
    const cleanAmount = cleanAmountCharacters.join('');
    return Number(cleanAmount);
  }

  const budgetItems: Array<BudgetItem> = [
    {
      id: 1,
      name: 'Groceries',
      amount: '2000.50',
      remainingAmount: '1995.02',
      transactionsNumber: 2,
      description: 'Groceries for the month',
    },
  ];
  const monthLastDay = new Date(year, month + 1, 0);
  const today = new Date(year, month, new Date().getDay());
  const [state, setState] = useState<State>({
    amount: 0,
    amountText: '0',
    day: undefined,
    budgetItemId: undefined,
    note: '',
  });

  return (
    <>
      <h2>
        <span>New Transaction for</span>{' '}
        {today.toLocaleDateString(undefined, {
          month: 'long',
          year: 'numeric',
        })}
      </h2>
      <form onSubmit={(e) => e.preventDefault()}>
        <label htmlFor="new-transaction-amount">Amount</label>
        <input
          id="new-transaction-amount"
          type="text"
          value={state.amountText}
          onChange={(event) => {
            const newAmount = transformAmount(event.target.value);
            const newAmountText = new Intl.NumberFormat().format(newAmount);
            setState((currentValue) => {
              return {
                ...currentValue,
                amountText: newAmountText,
                amount: newAmount,
              };
            });
          }}
        />

        <label htmlFor="new-transaction-budget-item-selector">
          Budget Category
        </label>
        <select id="new-transaction-budget-item-selector">
          <option>Choose a budget category</option>
          {budgetItems.map((budgetItem) => {
            return (
              <option value={budgetItem.id} key={budgetItem.id}>
                {budgetItem.name}
              </option>
            );
          })}
        </select>

        <label htmlFor="new-transaction-date">Transaction Date</label>
        <input
          id="new-transaction-date"
          type="date"
          min={`${year}-${month}-01`}
          max={`${year}-${month}-${monthLastDay}`}
          onChange={(event) => console.log(event.target.value)}
        />

        <label htmlFor="new-transaction-note">Note?</label>
        <textarea id="new-transaction-note" />

        <button className="card-button-base card-button-primary">Submit</button>
      </form>
    </>
  );
};

export default RecordTransaction;
