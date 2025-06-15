import {type Middleware, isRejectedWithValue } from '@reduxjs/toolkit'
import { setErrors } from '~/features/errors/errorSlice'
import {getInputErrors} from "~/base/helpers";

export const errorMiddleware: Middleware = store => next => action => {
    // @ts-ignore
    if (isRejectedWithValue(action) && action.payload.status === 400) {
        store.dispatch(setErrors(
            // @ts-ignore
            getInputErrors(action.payload)
        ));
    }

    return next(action)
}
