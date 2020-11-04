import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IUsluga, defaultValue } from 'app/shared/model/usluga.model';

export const ACTION_TYPES = {
  SEARCH_USLUGAS: 'usluga/SEARCH_USLUGAS',
  FETCH_USLUGA_LIST: 'usluga/FETCH_USLUGA_LIST',
  FETCH_USLUGA: 'usluga/FETCH_USLUGA',
  CREATE_USLUGA: 'usluga/CREATE_USLUGA',
  UPDATE_USLUGA: 'usluga/UPDATE_USLUGA',
  DELETE_USLUGA: 'usluga/DELETE_USLUGA',
  RESET: 'usluga/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IUsluga>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false,
};

export type UslugaState = Readonly<typeof initialState>;

// Reducer

export default (state: UslugaState = initialState, action): UslugaState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_USLUGAS):
    case REQUEST(ACTION_TYPES.FETCH_USLUGA_LIST):
    case REQUEST(ACTION_TYPES.FETCH_USLUGA):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_USLUGA):
    case REQUEST(ACTION_TYPES.UPDATE_USLUGA):
    case REQUEST(ACTION_TYPES.DELETE_USLUGA):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.SEARCH_USLUGAS):
    case FAILURE(ACTION_TYPES.FETCH_USLUGA_LIST):
    case FAILURE(ACTION_TYPES.FETCH_USLUGA):
    case FAILURE(ACTION_TYPES.CREATE_USLUGA):
    case FAILURE(ACTION_TYPES.UPDATE_USLUGA):
    case FAILURE(ACTION_TYPES.DELETE_USLUGA):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.SEARCH_USLUGAS):
    case SUCCESS(ACTION_TYPES.FETCH_USLUGA_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.FETCH_USLUGA):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_USLUGA):
    case SUCCESS(ACTION_TYPES.UPDATE_USLUGA):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_USLUGA):
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

const apiUrl = 'api/uslugas';
const apiSearchUrl = 'api/_search/uslugas';

// Actions

export const getSearchEntities: ICrudSearchAction<IUsluga> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_USLUGAS,
  payload: axios.get<IUsluga>(`${apiSearchUrl}?query=${query}`),
});

export const getEntities: ICrudGetAllAction<IUsluga> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_USLUGA_LIST,
  payload: axios.get<IUsluga>(`${apiUrl}?cacheBuster=${new Date().getTime()}`),
});

export const getEntity: ICrudGetAction<IUsluga> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_USLUGA,
    payload: axios.get<IUsluga>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IUsluga> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_USLUGA,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IUsluga> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_USLUGA,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IUsluga> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_USLUGA,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
