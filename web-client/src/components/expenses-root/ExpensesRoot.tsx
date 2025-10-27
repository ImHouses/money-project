import BudgetItemList from '../budget-item-list/BudgetItemList';
import { useNavigate } from 'react-router';

const ExpensesRoot = () => {
  const navigate = useNavigate();

  return (
    <>
      <h2>
        <span>Expenses for</span> October 2025
      </h2>
      <div className="month-selectors">
        <button className="card-button-base card-button-secondary">
          &#8592; Previous
        </button>
        <button className="card-button-base card-button-secondary">
          Next &#8594;
        </button>
        <button
          className="card-button-base card-button-primary"
          id="new-transaction"
          onClick={() => navigate('/transactions/new')}
        >
          + New Transaction
        </button>
      </div>
      <BudgetItemList />
    </>
  );
};

export default ExpensesRoot;
