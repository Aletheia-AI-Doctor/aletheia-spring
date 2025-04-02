import { type RouteConfig, index, route, layout } from "@react-router/dev/routes";

export default [
    layout("./layout.tsx", [
        index("routes/home.tsx"),
        route("/diagnose/:model?", "routes/diagnose.tsx"),
    ]),

] satisfies RouteConfig;
