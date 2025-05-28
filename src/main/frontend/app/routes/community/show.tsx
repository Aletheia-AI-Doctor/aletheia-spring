import type { Route } from "./+types/show";

export function meta({}: Route.MetaArgs) {
    return [
        { title: "Doxa Community - Post" },
        { name: "description", content: "View post" },
    ];
}


export default function DoxaPost() {
    return (
        <div>
            <h1>Doxa</h1>
            <p>Welcome to the Doxa community!</p>
        </div>
    );
}