import type { Route } from "./+types/home";

export function meta({}: Route.MetaArgs) {
  return [
    { title: "Aletheia AI Doctor" },
    { name: "description", content: "Welcome to React Router!" },
  ];
}

export default function Home() {
  return (
      <div className="flex flex-col space-y-6">
        <a>Home</a>
        <a href="/diagnose">Diagnose</a>
        <a href="/profile">Profile</a>
      </div>
  );
}
