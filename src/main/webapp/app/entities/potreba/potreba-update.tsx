import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './potreba.reducer';
import { IPotreba } from 'app/shared/model/potreba.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IPotrebaUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const PotrebaUpdate = (props: IPotrebaUpdateProps) => {
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { potrebaEntity, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/potreba');
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...potrebaEntity,
        ...values,
      };

      if (isNew) {
        props.createEntity(entity);
      } else {
        props.updateEntity(entity);
      }
    }
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="popravljanjeApp.potreba.home.createOrEditLabel">
            <Translate contentKey="popravljanjeApp.potreba.home.createOrEditLabel">Create or edit a Potreba</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : potrebaEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="potreba-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="potreba-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="cijenaMinLabel" for="potreba-cijenaMin">
                  <Translate contentKey="popravljanjeApp.potreba.cijenaMin">Cijena Min</Translate>
                </Label>
                <AvField id="potreba-cijenaMin" type="string" className="form-control" name="cijenaMin" />
              </AvGroup>
              <AvGroup>
                <Label id="cijenaMaxLabel" for="potreba-cijenaMax">
                  <Translate contentKey="popravljanjeApp.potreba.cijenaMax">Cijena Max</Translate>
                </Label>
                <AvField id="potreba-cijenaMax" type="string" className="form-control" name="cijenaMax" />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/potreba" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </AvForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

const mapStateToProps = (storeState: IRootState) => ({
  potrebaEntity: storeState.potreba.entity,
  loading: storeState.potreba.loading,
  updating: storeState.potreba.updating,
  updateSuccess: storeState.potreba.updateSuccess,
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(PotrebaUpdate);
