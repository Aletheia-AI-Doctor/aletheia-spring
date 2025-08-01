import {type Action, type ThunkAction} from "@reduxjs/toolkit"
import { combineSlices, configureStore } from "@reduxjs/toolkit"
import { setupListeners } from "@reduxjs/toolkit/query"
import {scansApiSlice} from "~/features/scans/scansApiSlice";
import {authenticationApiSlice, authSlice} from "~/features/authentication/authenticationApiSlice";
import { patientsApiSlice } from "~/features/patient/patientApiSlice";
import { doctorApiSlice } from "~/features/doctor/doctorApiSlice";
import {doctorDashboardApiSlice} from "~/features/doctor/doctorDashboardApiSlice";
import {hospitalApiSlice} from "~/features/hospital/hospitalApiSlice";
import {doctorActivityApiSlice} from "~/features/doctor/doctorActivityApiSlice";
import {postsApiSlice} from "~/features/community/postsApiSlice";
import {diagnosisApiSlice} from "~/features/diagnosis/diagnosisApiSlice";
import { errorMiddleware } from "~/features/errors/globalErrorMiddleware";
import errorsReducer from "~/features/errors/errorSlice";
import notificationReducer from "~/features/notifications/notificationSlice";
import { notificationApiSlice } from "~/features/community/notficationApiSlice";
const rootReducer = combineSlices(
    scansApiSlice,
    authenticationApiSlice,
    authSlice,
    patientsApiSlice,
    doctorApiSlice,
    doctorDashboardApiSlice,
    hospitalApiSlice,
    doctorActivityApiSlice,
    postsApiSlice,
    notificationApiSlice,
    diagnosisApiSlice,
    {
        reducerPath: 'globalErrors',
        reducer: errorsReducer,
    },
    {
        reducerPath: 'notifications',
        reducer: notificationReducer,
    },
)
export type RootState = ReturnType<typeof rootReducer>


export const makeStore = (preloadedState?: Partial<RootState>) => {
    const store = configureStore({
        reducer: rootReducer,


        middleware: getDefaultMiddleware => {
            return getDefaultMiddleware({
                serializableCheck: false
            })
                .concat(errorMiddleware)
                .concat(scansApiSlice.middleware)
                .concat(authenticationApiSlice.middleware)
                .concat(patientsApiSlice.middleware)
                .concat(doctorApiSlice.middleware)
                .concat(doctorDashboardApiSlice.middleware)
                .concat(doctorActivityApiSlice.middleware)
                .concat(postsApiSlice.middleware)
                .concat(hospitalApiSlice.middleware)
                .concat(diagnosisApiSlice.middleware)
                .concat(notificationApiSlice.middleware);

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
