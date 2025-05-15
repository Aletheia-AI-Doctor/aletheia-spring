import type {Route} from "./+types/confirm-email";
import { useSearchParams } from 'react-router';

const ConfirmEmail = () => {
    const [searchParams] = useSearchParams();
    const status = searchParams.get('status');

    return (
        <div>
            {status === 'success' ? (
                <div>Email confirmed successfully!</div>
            ) : status === 'error' ? (
                <div>Confirmation failed. Invalid or expired token.</div>
            ) : (
                <div>Loading confirmation...</div>
            )}
        </div>
    );
}

export default ConfirmEmail;