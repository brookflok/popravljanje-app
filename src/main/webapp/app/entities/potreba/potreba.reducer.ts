import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IPotreba, defaultValue } from 'app/shared/model/potreba.model';

export const ACTION_TYPES = {
  SEARCH_POTREBAS: 'potreba/SEARCH_POTREBAS',
  FETCH_POTREBA_LIST: 'potreba/FETCH_POTREBA_LIST',
  FETCH_POTREBA: 'potreba/FETCH_POTREBA',
  CREATE_POTREBA: 'potreba/CREATE_POTREBA',
  UPDATE_POTREBA: 'potreba/UPDATE_POTREBA',
  DELETE_POTREBA: 'potreba/DELETE_POTREBA',
  RESET: 'potreba/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IPotreba>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false,
};

export type PotrebaState = Readonly<typeof initialState>;

// Reducer

export default (state: PotrebaState = initialState, action): PotrebaState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_POTREBAS):
    case REQUEST(ACTION_TYPES.FETCH_POTREBA_LIST):
    case REQUEST(ACTION_TYPES.FETCH_POTREBA):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_POTREBA):
    case REQUEST(ACTION_TYPES.UPDATE_POTREBA):
    case REQUEST(ACTION_TYPES.DELETE_POTREBA):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.SEARCH_POTREBAS):
    case FAILURE(ACTION_TYPES.FETCH_POTREBA_LIST):
    case FAILURE(ACTION_TYPES.FETCH_POTREBA):
    case FAILURE(ACTION_TYPES.CREATE_POTREBA):
    case FAILURE(ACTION_TYPES.UPDATE_POTREBA):
    case FAILURE(ACTION_TYPES.DELETE_POTREBA):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.SEARCH_POTREBAS):
    case SUCCESS(ACTION_TYPES.FETCH_POTREBA_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.FETCH_POTREBA):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_POTREBA):
    case SUCCESS(ACTION_TYPES.UPDATE_POTREBA):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_POTREBA):
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

const apiUrl = 'api/potrebas';
const apiSearchUrl = 'api/_search/potrebas';

// Actions

export const getSearchEntities: ICrudSearchAction<IPotreba> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_POTREBAS,
  payload: axios.get<IPotreba>(`${apiSearchUrl}?query=${query}`),
});

export const getEntities: ICrudGetAllAction<IPotreba> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_POTREBA_LIST,
  payload: axios.get<IPotreba>(`${apiUrl}?cacheBuster=${new Date().getTime()}`),
});

export const getEntity: ICrudGetAction<IPotreba> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_POTREBA,
    payload: axios.get<IPotreba>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IPotreba> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_POTREBA,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IPotreba> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_POTREBA,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IPotreba> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_POTREBA,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
