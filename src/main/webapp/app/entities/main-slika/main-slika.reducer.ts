import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IMainSlika, defaultValue } from 'app/shared/model/main-slika.model';

export const ACTION_TYPES = {
  SEARCH_MAINSLIKAS: 'mainSlika/SEARCH_MAINSLIKAS',
  FETCH_MAINSLIKA_LIST: 'mainSlika/FETCH_MAINSLIKA_LIST',
  FETCH_MAINSLIKA: 'mainSlika/FETCH_MAINSLIKA',
  CREATE_MAINSLIKA: 'mainSlika/CREATE_MAINSLIKA',
  UPDATE_MAINSLIKA: 'mainSlika/UPDATE_MAINSLIKA',
  DELETE_MAINSLIKA: 'mainSlika/DELETE_MAINSLIKA',
  RESET: 'mainSlika/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IMainSlika>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false,
};

export type MainSlikaState = Readonly<typeof initialState>;

// Reducer

export default (state: MainSlikaState = initialState, action): MainSlikaState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_MAINSLIKAS):
    case REQUEST(ACTION_TYPES.FETCH_MAINSLIKA_LIST):
    case REQUEST(ACTION_TYPES.FETCH_MAINSLIKA):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_MAINSLIKA):
    case REQUEST(ACTION_TYPES.UPDATE_MAINSLIKA):
    case REQUEST(ACTION_TYPES.DELETE_MAINSLIKA):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.SEARCH_MAINSLIKAS):
    case FAILURE(ACTION_TYPES.FETCH_MAINSLIKA_LIST):
    case FAILURE(ACTION_TYPES.FETCH_MAINSLIKA):
    case FAILURE(ACTION_TYPES.CREATE_MAINSLIKA):
    case FAILURE(ACTION_TYPES.UPDATE_MAINSLIKA):
    case FAILURE(ACTION_TYPES.DELETE_MAINSLIKA):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.SEARCH_MAINSLIKAS):
    case SUCCESS(ACTION_TYPES.FETCH_MAINSLIKA_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.FETCH_MAINSLIKA):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_MAINSLIKA):
    case SUCCESS(ACTION_TYPES.UPDATE_MAINSLIKA):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_MAINSLIKA):
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

const apiUrl = 'api/main-slikas';
const apiSearchUrl = 'api/_search/main-slikas';

// Actions

export const getSearchEntities: ICrudSearchAction<IMainSlika> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_MAINSLIKAS,
  payload: axios.get<IMainSlika>(`${apiSearchUrl}?query=${query}`),
});

export const getEntities: ICrudGetAllAction<IMainSlika> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_MAINSLIKA_LIST,
  payload: axios.get<IMainSlika>(`${apiUrl}?cacheBuster=${new Date().getTime()}`),
});

export const getEntity: ICrudGetAction<IMainSlika> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_MAINSLIKA,
    payload: axios.get<IMainSlika>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IMainSlika> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_MAINSLIKA,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IMainSlika> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_MAINSLIKA,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IMainSlika> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_MAINSLIKA,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
