import { Routes, Route } from 'react-router-dom';
import Dashboard from './dashboard/Dashboard';
import Coin from './coin/Coin';
const Main = () => {
return (         
  <Routes>
    <Route path='/' element={<Dashboard/>} />
    <Route path='/coinChart' element={<Coin/>} />
    <Route path="*" element={<Dashboard/>} />
  </Routes>
);
}
export default Main;