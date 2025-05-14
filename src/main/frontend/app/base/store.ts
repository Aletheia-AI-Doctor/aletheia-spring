import {type Action, type ThunkAction} from "@reduxjs/toolkit"
import { combineSlices, configureStore } from "@reduxjs/toolkit"
import { setupListeners } from "@reduxjs/toolkit/query"
import {scansApiSlice} from "~/features/scans/scansApiSlice";
import {authenticationApiSlice, authSlice} from "~/features/authentication/authenticationApiSlice";
import { patientsApiSlice } from "~/features/patient/patientApiSlice";
import { doctorApiSlice } from "~/features/doctor/doctorApiSlice";
import {doctorDashboardApiSlice} from "~/features/doctor/doctorDashboardApiSlice";
import {hospitalApiSlice} from "~/features/hospital/hospitalApiSlice";

const rootReducer = combineSlices(
    scansApiSlice,
    authenticationApiSlice,
    authSlice,
    patientsApiSlice,
    doctorApiSlice,
    doctorDashboardApiSlice,
    hospitalApiSlice,
)
export type RootState = ReturnType<typeof rootReducer>


export const makeStore = (preloadedState?: Partial<RootState>) => {
    const store = configureStore({
        reducer: rootReducer,


        middleware: getDefaultMiddleware => {
            return getDefaultMiddleware({
                serializableCheck: false
            })
                .concat(scansApiSlice.middleware)
                .concat(authenticationApiSlice.middleware)
                .concat(patientsApiSlice.middleware)
                .concat(doctorApiSlice.middleware)
                .concat(doctorDashboardApiSlice.middleware)
                .concat(hospitalApiSlice.middleware);

        },
        preloadedState
    })
    setupListeners(store.dispatch)
    return store
}

export const store = makeStore()

// Infer the type of `store`
export type AppStore = typeof store
// Infer the `AppDispatch` type from the store itself
export type AppDispatch = AppStore["dispatch"]
export type AppThunk<ThunkReturnType = void> = ThunkAction<
    ThunkReturnType,
    RootState,
    unknown,
    Action
>
