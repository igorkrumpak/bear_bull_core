import React from 'react';
import './App.css';
import Dashboard from './components/dashboard/Dashboard';
import { Breadcrumb, Image, Layout, Menu, Space, theme, Tooltip, Typography } from 'antd';
import { Content, Footer, Header } from 'antd/es/layout/layout';
import Main from './components/Main';
import Title from 'antd/es/typography/Title';
import { useNavigate } from 'react-router-dom';

function App() {

  const navigate = useNavigate();

  const {
    token: { colorBgContainer },
  } = theme.useToken();

  function navigateToRoot() {
    navigate({ pathname: "/"})
}


  return (
    <Layout>
      <Tooltip>
      <Header  className="pointer" style={{ display: "flex", alignItems: "center" }} onClick={navigateToRoot}>
        <Image
          height={64}
          src="/logo_bear_bull.png"
          alt="Logo"
          preview={false}
        />
        <Title
          level={3}
          style={{
            color: "white",
            display: "inline-block",
            marginLeft: "10px",
            fontWeight: "bold",
            letterSpacing: "0.05em",
            textTransform: "uppercase",
          }}
        >
          Bear Bull
        </Title>
      </Header>
      </Tooltip>
      <Content style={{ padding: '0 50px' }}>
        <div className="site-layout-content" style={{ background: colorBgContainer }}>
          <Main />
        </div>
      </Content>
      <Footer style={{ textAlign: 'center' }}>IITech ©2023 Created by Igor</Footer>

    </Layout>
  );

}

export default App;
