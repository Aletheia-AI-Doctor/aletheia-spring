import { type RouteConfig, index, route, layout } from "@react-router/dev/routes";

export default [
    layout("./layout.tsx", [
        index("routes/home.tsx"),
        route("/diagnose/:model?", "routes/diagnose.tsx"),
        route("/profile", "routes/profile.tsx", {
            loader: () => null, // No pre-fetching needed
        }),
    ]),

    layout("./routes/auth/layout.tsx", [
        route("login", "./routes/auth/login.tsx"),
    ]),

] satisfies RouteConfig;
