import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ILokacija, defaultValue } from 'app/shared/model/lokacija.model';

export const ACTION_TYPES = {
  SEARCH_LOKACIJAS: 'lokacija/SEARCH_LOKACIJAS',
  FETCH_LOKACIJA_LIST: 'lokacija/FETCH_LOKACIJA_LIST',
  FETCH_LOKACIJA: 'lokacija/FETCH_LOKACIJA',
  CREATE_LOKACIJA: 'lokacija/CREATE_LOKACIJA',
  UPDATE_LOKACIJA: 'lokacija/UPDATE_LOKACIJA',
  DELETE_LOKACIJA: 'lokacija/DELETE_LOKACIJA',
  RESET: 'lokacija/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ILokacija>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false,
};

export type LokacijaState = Readonly<typeof initialState>;

// Reducer

export default (state: LokacijaState = initialState, action): LokacijaState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_LOKACIJAS):
    case REQUEST(ACTION_TYPES.FETCH_LOKACIJA_LIST):
    case REQUEST(ACTION_TYPES.FETCH_LOKACIJA):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_LOKACIJA):
    case REQUEST(ACTION_TYPES.UPDATE_LOKACIJA):
    case REQUEST(ACTION_TYPES.DELETE_LOKACIJA):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.SEARCH_LOKACIJAS):
    case FAILURE(ACTION_TYPES.FETCH_LOKACIJA_LIST):
    case FAILURE(ACTION_TYPES.FETCH_LOKACIJA):
    case FAILURE(ACTION_TYPES.CREATE_LOKACIJA):
    case FAILURE(ACTION_TYPES.UPDATE_LOKACIJA):
    case FAILURE(ACTION_TYPES.DELETE_LOKACIJA):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.SEARCH_LOKACIJAS):
    case SUCCESS(ACTION_TYPES.FETCH_LOKACIJA_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.FETCH_LOKACIJA):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_LOKACIJA):
    case SUCCESS(ACTION_TYPES.UPDATE_LOKACIJA):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_LOKACIJA):
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

const apiUrl = 'api/lokacijas';
const apiSearchUrl = 'api/_search/lokacijas';

// Actions

export const getSearchEntities: ICrudSearchAction<ILokacija> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_LOKACIJAS,
  payload: axios.get<ILokacija>(`${apiSearchUrl}?query=${query}`),
});

export const getEntities: ICrudGetAllAction<ILokacija> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_LOKACIJA_LIST,
  payload: axios.get<ILokacija>(`${apiUrl}?cacheBuster=${new Date().getTime()}`),
});

export const getEntity: ICrudGetAction<ILokacija> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_LOKACIJA,
    payload: axios.get<ILokacija>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<ILokacija> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_LOKACIJA,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ILokacija> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_LOKACIJA,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<ILokacija> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_LOKACIJA,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
