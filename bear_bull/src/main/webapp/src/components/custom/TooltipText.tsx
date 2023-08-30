import QuestionCircleOutlined from '@ant-design/icons/lib/icons/QuestionCircleOutlined';
import { Tooltip } from 'antd';
import axios from 'axios';
import React from 'react';
import { useState } from 'react';
import parse from 'html-react-parser';

function TooltipText({ notation }: { notation: string }) {

    const [description, setDesciption] = useState<string>();

    const getDesciption = async () => {
        axios("/api/dashboard/description/" + notation)
            .then((response) => {
                setDesciption(response.data);
            });
    }

    React.useEffect(() => {
        getDesciption();
    }, []);

    return (
        <Tooltip overlay= {description != null ? parse(description) : ''}>
            <QuestionCircleOutlined />
        </Tooltip>
    )
}
export default TooltipText;