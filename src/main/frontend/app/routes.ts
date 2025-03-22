import { type RouteConfig, index, route, layout } from "@react-router/dev/routes";

export default [
    layout("./layout.tsx", [
        index("routes/home.tsx"),
        route("/diagnose", "routes/diagnose.tsx"),
    ]),

] satisfies RouteConfig;
