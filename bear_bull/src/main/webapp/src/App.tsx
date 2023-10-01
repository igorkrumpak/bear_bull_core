import React, { useState } from 'react';
import './App.css';
import { Image, Layout, Menu, MenuProps, theme, } from 'antd';
import { Content, Footer } from 'antd/es/layout/layout';
import Main from './components/Main';
import { useNavigate } from 'react-router-dom';
import { CalendarOutlined } from '@ant-design/icons';
import Sider from 'antd/es/layout/Sider';

function App() {

  const navigate = useNavigate();

  const {
    token: { colorBgContainer },
  } = theme.useToken();

  function navigateToRoot() {
    navigate({ pathname: "/" })
  }


  type MenuItem = Required<MenuProps>['items'][number];

  function getItem(
    label: React.ReactNode,
    key: React.Key,
    icon?: React.ReactNode,
    children?: MenuItem[],
  ): MenuItem {
    return {
      key,
      icon,
      children,
      label,
    } as MenuItem;
  }
  const items: MenuItem[] = [

    getItem('Timeframe', 'timeframe', <CalendarOutlined />, [
      getItem('Daily', 'daily'),
      getItem('Monthly', 'weekly'),
    ])
  ];


  const [collapsed, setCollapsed] = useState(true);

  return (
    <Layout style={{ minHeight: '100vh' }}>
      <Sider collapsible collapsed={collapsed} onCollapse={(value) => setCollapsed(value)}>
        <Image
          className="pointer"
          onClick={navigateToRoot}
          src="/logo_bear_bull.png"
          alt="Logo"
          preview={false}
        />
        <Menu theme="dark" defaultSelectedKeys={['1']} mode="inline" items={items} />
      </Sider>
      <Layout>
        <Content style={{ margin: '0 16px' }}>
          <div style={{ padding: 24, minHeight: 360, background: colorBgContainer }}>
            <Main />
          </div>
        </Content>
        <Footer style={{ textAlign: 'center' }}>IITech ©2023 Created by Igor</Footer>
      </Layout>
    </Layout>
  );

}

export default App;
