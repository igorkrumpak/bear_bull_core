import './App.css';
import { Layout,  theme, } from 'antd';
import { Content, Footer } from 'antd/es/layout/layout';
import Main from './components/Main';
import { GlobalStateProvider } from './state/GlobalStateProvider';
import SideMenu from './components/SideMenu';


function App() {
 
  const {
    token: { colorBgContainer },
  } = theme.useToken();

  return (
    <GlobalStateProvider value={{timeframe : "DAILY"}}>
      <Layout style={{ minHeight: '100vh' }}>
        <SideMenu />
        <Layout>
          <Content style={{ margin: '0 16px' }}>
            <div style={{ padding: 24, minHeight: 360, background: colorBgContainer }}>
              <Main />
            </div>
          </Content>
          <Footer style={{ textAlign: 'center' }}>IITech ©2023 Created by Igor</Footer>
        </Layout>
      </Layout>
    </GlobalStateProvider>
  );

}

export default App;
