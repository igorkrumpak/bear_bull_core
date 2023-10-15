import CalendarOutlined from "@ant-design/icons/lib/icons/CalendarOutlined";
import Sider from "antd/es/layout/Sider";
import { useState } from "react";
import { Image,  Menu, MenuProps, } from 'antd';
import { useNavigate } from "react-router-dom";
import { useGlobalState } from "../state/GlobalStateProvider";

const SideMenu = () => {

    const navigate = useNavigate();
    const { setState } = useGlobalState();
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
            getItem('Daily', 'DAILY'),
            getItem('Weekly', 'WEEKLY'),
        ])
    ];

    const [collapsed, setCollapsed] = useState(true);

    const onMenuClick = (e: any) => {
        setState((prevState) => ({
            ...prevState,
            timeframe: e.key,
        }));
    };
    
    return (
        <Sider collapsible collapsed={collapsed} onCollapse={(value) => setCollapsed(value)}>
            <Image
                className="pointer"
                onClick={navigateToRoot}
                src="/logo_bear_bull.png"
                alt="Logo"
                preview={false}
            />
            <Menu theme="dark" onClick={onMenuClick} defaultSelectedKeys={['DAILY']} mode="inline" items={items} />
        </Sider>
    );
}
export default SideMenu;