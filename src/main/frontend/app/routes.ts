import { type RouteConfig, index, route, layout } from "@react-router/dev/routes";

export default [
    layout("./layout.tsx", [
        index("routes/home.tsx"),
        route("/diagnose/:model?", "routes/diagnose.tsx"),
        route("/patient", "routes/patient/showPatient.tsx"),
        route("/profile", "routes/profile.tsx"),
        route("/dashboard","routes/dashboard.tsx")
    ]),

    layout("./routes/auth/layout.tsx", [
        route("login", "./routes/auth/login.tsx"),
    ]),
    

] satisfies RouteConfig;
