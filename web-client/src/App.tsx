import './App.css';
import { BrowserRouter, Routes, Route } from 'react-router';
import ExpensesRoot from './components/expenses-root/ExpensesRoot';
import RecordTransaction from './components/record-transaction/RecordTransaction';

function App() {
  const today = new Date();

  return (
    <>
      <div className="card">
        <BrowserRouter>
          <Routes>
            <Route path="/" element={<ExpensesRoot />} />
            <Route
              path="/transactions/new"
              element={
                // Pass today.getMonth() + 1 given that today.getMonth() is zero based
                // but not the Date constructor.
                <RecordTransaction
                  month={today.getMonth() + 1}
                  year={today.getFullYear()}
                />
              }
            />
          </Routes>
        </BrowserRouter>
      </div>
    </>
  );
}

export default App;
