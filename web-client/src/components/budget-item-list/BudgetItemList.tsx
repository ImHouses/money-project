import BudgetItem from '../budget-item/BudgetItem';

const BudgetItemList = () => {
  return (
    <div className="budget-items-container">
      {
        <>
          <BudgetItem />
          <BudgetItem />
          <BudgetItem />
        </>
      }
    </div>
  );
};

export default BudgetItemList;
