import '../../models/BudgetItem';
import './BudgetItem.css';

const BudgetItem = () => {
  return (
    <div className="budget-item">
      <h3>Car payment 🚗</h3>
      <p className="budget-item-description">
        This is the description (if any of course)
      </p>
      <p className="budget-item-amount">Set amount $11,800</p>
      <div className="budget-item-remaining">
        <p>Remaining</p>
        <div className="line-fill"></div>
        <p className="budget-item-remaining-amount">$11,800</p>
      </div>
      <p className="budget-item-transactions">
        Transactions: <span>32</span>
      </p>
    </div>
  );
};

export default BudgetItem;
