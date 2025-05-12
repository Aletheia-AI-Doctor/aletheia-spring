import {Outlet, useNavigate} from "react-router";
import {useEffect} from "react";
import {useAppSelector} from "~/base/hooks";

export default function LoginLayout() {
    const navigate = useNavigate();
    const token = useAppSelector((state) => state.auth.token);

    useEffect(() => {
        if (token) {
            navigate("/", { replace: true });
        }
    }, [token, navigate]);

    return (
        <div>
            {!token ? <Outlet/> : null};
        </div>
    );
}
