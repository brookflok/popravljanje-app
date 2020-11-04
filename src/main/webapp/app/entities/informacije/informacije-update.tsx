import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IArtikl } from 'app/shared/model/artikl.model';
import { getEntities as getArtikls } from 'app/entities/artikl/artikl.reducer';
import { getEntity, updateEntity, createEntity, reset } from './informacije.reducer';
import { IInformacije } from 'app/shared/model/informacije.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IInformacijeUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const InformacijeUpdate = (props: IInformacijeUpdateProps) => {
  const [artiklId, setArtiklId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { informacijeEntity, artikls, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/informacije');
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getArtikls();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    values.datumObjave = convertDateTimeToServer(values.datumObjave);

    if (errors.length === 0) {
      const entity = {
        ...informacijeEntity,
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
          <h2 id="popravljanjeApp.informacije.home.createOrEditLabel">
            <Translate contentKey="popravljanjeApp.informacije.home.createOrEditLabel">Create or edit a Informacije</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : informacijeEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="informacije-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="informacije-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="vrstaOglasaLabel" for="informacije-vrstaOglasa">
                  <Translate contentKey="popravljanjeApp.informacije.vrstaOglasa">Vrsta Oglasa</Translate>
                </Label>
                <AvField id="informacije-vrstaOglasa" type="text" name="vrstaOglasa" />
              </AvGroup>
              <AvGroup>
                <Label id="datumObjaveLabel" for="informacije-datumObjave">
                  <Translate contentKey="popravljanjeApp.informacije.datumObjave">Datum Objave</Translate>
                </Label>
                <AvInput
                  id="informacije-datumObjave"
                  type="datetime-local"
                  className="form-control"
                  name="datumObjave"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.informacijeEntity.datumObjave)}
                />
              </AvGroup>
              <AvGroup>
                <Label id="brojPregledaLabel" for="informacije-brojPregleda">
                  <Translate contentKey="popravljanjeApp.informacije.brojPregleda">Broj Pregleda</Translate>
                </Label>
                <AvField id="informacije-brojPregleda" type="string" className="form-control" name="brojPregleda" />
              </AvGroup>
              <AvGroup>
                <Label for="informacije-artikl">
                  <Translate contentKey="popravljanjeApp.informacije.artikl">Artikl</Translate>
                </Label>
                <AvInput id="informacije-artikl" type="select" className="form-control" name="artikl.id">
                  <option value="" key="0" />
                  {artikls
                    ? artikls.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/informacije" replace color="info">
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
  artikls: storeState.artikl.entities,
  informacijeEntity: storeState.informacije.entity,
  loading: storeState.informacije.loading,
  updating: storeState.informacije.updating,
  updateSuccess: storeState.informacije.updateSuccess,
});

const mapDispatchToProps = {
  getArtikls,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(InformacijeUpdate);
