import { Tag } from "antd";

function CoinTags({ tags }: { tags: string }) {
    return (
        <span>
            {tags && tags.split(',').map((tag) => {
                return <Tag color={tag.split(';')[1]} key={tag.split(';')[0]}>
                    {tag.split(';')[0]}
                </Tag>
            })}
        </span>
    );
}

export default CoinTags;