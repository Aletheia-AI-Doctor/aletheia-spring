import {Link, type LinkProps} from "react-router";

export default function StyledLink(props: LinkProps) {
    return (
        <Link {...props} className="text-blue-600 hover:text-blue-800">
            {props.children}
        </Link>
    );
}