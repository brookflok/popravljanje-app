import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IInformacije, defaultValue } from 'app/shared/model/informacije.model';

export const ACTION_TYPES = {
  SEARCH_INFORMACIJES: 'informacije/SEARCH_INFORMACIJES',
  FETCH_INFORMACIJE_LIST: 'informacije/FETCH_INFORMACIJE_LIST',
  FETCH_INFORMACIJE: 'informacije/FETCH_INFORMACIJE',
  CREATE_INFORMACIJE: 'informacije/CREATE_INFORMACIJE',
  UPDATE_INFORMACIJE: 'informacije/UPDATE_INFORMACIJE',
  DELETE_INFORMACIJE: 'informacije/DELETE_INFORMACIJE',
  RESET: 'informacije/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IInformacije>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false,
};

export type InformacijeState = Readonly<typeof initialState>;

// Reducer

export default (state: InformacijeState = initialState, action): InformacijeState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_INFORMACIJES):
    case REQUEST(ACTION_TYPES.FETCH_INFORMACIJE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_INFORMACIJE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_INFORMACIJE):
    case REQUEST(ACTION_TYPES.UPDATE_INFORMACIJE):
    case REQUEST(ACTION_TYPES.DELETE_INFORMACIJE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.SEARCH_INFORMACIJES):
    case FAILURE(ACTION_TYPES.FETCH_INFORMACIJE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_INFORMACIJE):
    case FAILURE(ACTION_TYPES.CREATE_INFORMACIJE):
    case FAILURE(ACTION_TYPES.UPDATE_INFORMACIJE):
    case FAILURE(ACTION_TYPES.DELETE_INFORMACIJE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.SEARCH_INFORMACIJES):
    case SUCCESS(ACTION_TYPES.FETCH_INFORMACIJE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.FETCH_INFORMACIJE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_INFORMACIJE):
    case SUCCESS(ACTION_TYPES.UPDATE_INFORMACIJE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_INFORMACIJE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {},
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState,
      };
    default:
      return state;
  }
};

const apiUrl = 'api/informacijes';
const apiSearchUrl = 'api/_search/informacijes';

// Actions

export const getSearchEntities: ICrudSearchAction<IInformacije> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_INFORMACIJES,
  payload: axios.get<IInformacije>(`${apiSearchUrl}?query=${query}`),
});

export const getEntities: ICrudGetAllAction<IInformacije> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_INFORMACIJE_LIST,
  payload: axios.get<IInformacije>(`${apiUrl}?cacheBuster=${new Date().getTime()}`),
});

export const getEntity: ICrudGetAction<IInformacije> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_INFORMACIJE,
    payload: axios.get<IInformacije>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IInformacije> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_INFORMACIJE,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IInformacije> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_INFORMACIJE,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IInformacije> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_INFORMACIJE,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
