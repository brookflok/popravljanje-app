import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IDodatniInfoUser } from 'app/shared/model/dodatni-info-user.model';
import { getEntities as getDodatniInfoUsers } from 'app/entities/dodatni-info-user/dodatni-info-user.reducer';
import { IArtikl } from 'app/shared/model/artikl.model';
import { getEntities as getArtikls } from 'app/entities/artikl/artikl.reducer';
import { IOdgovorNaJavnoPitanje } from 'app/shared/model/odgovor-na-javno-pitanje.model';
import { getEntities as getOdgovorNaJavnoPitanjes } from 'app/entities/odgovor-na-javno-pitanje/odgovor-na-javno-pitanje.reducer';
import { getEntity, updateEntity, createEntity, reset } from './javno-pitanje.reducer';
import { IJavnoPitanje } from 'app/shared/model/javno-pitanje.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IJavnoPitanjeUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const JavnoPitanjeUpdate = (props: IJavnoPitanjeUpdateProps) => {
  const [dodatniinfoUserId, setDodatniinfoUserId] = useState('0');
  const [artiklId, setArtiklId] = useState('0');
  const [odgovorNaJavnoPitanjeId, setOdgovorNaJavnoPitanjeId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { javnoPitanjeEntity, dodatniInfoUsers, artikls, odgovorNaJavnoPitanjes, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/javno-pitanje');
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getDodatniInfoUsers();
    props.getArtikls();
    props.getOdgovorNaJavnoPitanjes();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    values.datum = convertDateTimeToServer(values.datum);

    if (errors.length === 0) {
      const entity = {
        ...javnoPitanjeEntity,
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
          <h2 id="popravljanjeApp.javnoPitanje.home.createOrEditLabel">
            <Translate contentKey="popravljanjeApp.javnoPitanje.home.createOrEditLabel">Create or edit a JavnoPitanje</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : javnoPitanjeEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="javno-pitanje-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="javno-pitanje-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="pitanjeLabel" for="javno-pitanje-pitanje">
                  <Translate contentKey="popravljanjeApp.javnoPitanje.pitanje">Pitanje</Translate>
                </Label>
                <AvField id="javno-pitanje-pitanje" type="text" name="pitanje" />
              </AvGroup>
              <AvGroup>
                <Label id="datumLabel" for="javno-pitanje-datum">
                  <Translate contentKey="popravljanjeApp.javnoPitanje.datum">Datum</Translate>
                </Label>
                <AvInput
                  id="javno-pitanje-datum"
                  type="datetime-local"
                  className="form-control"
                  name="datum"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.javnoPitanjeEntity.datum)}
                />
              </AvGroup>
              <AvGroup check>
                <Label id="prikazLabel">
                  <AvInput id="javno-pitanje-prikaz" type="checkbox" className="form-check-input" name="prikaz" />
                  <Translate contentKey="popravljanjeApp.javnoPitanje.prikaz">Prikaz</Translate>
                </Label>
              </AvGroup>
              <AvGroup>
                <Label for="javno-pitanje-dodatniinfoUser">
                  <Translate contentKey="popravljanjeApp.javnoPitanje.dodatniinfoUser">Dodatniinfo User</Translate>
                </Label>
                <AvInput id="javno-pitanje-dodatniinfoUser" type="select" className="form-control" name="dodatniinfoUser.id">
                  <option value="" key="0" />
                  {dodatniInfoUsers
                    ? dodatniInfoUsers.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="javno-pitanje-artikl">
                  <Translate contentKey="popravljanjeApp.javnoPitanje.artikl">Artikl</Translate>
                </Label>
                <AvInput id="javno-pitanje-artikl" type="select" className="form-control" name="artikl.id">
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
              <Button tag={Link} id="cancel-save" to="/javno-pitanje" replace color="info">
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
  dodatniInfoUsers: storeState.dodatniInfoUser.entities,
  artikls: storeState.artikl.entities,
  odgovorNaJavnoPitanjes: storeState.odgovorNaJavnoPitanje.entities,
  javnoPitanjeEntity: storeState.javnoPitanje.entity,
  loading: storeState.javnoPitanje.loading,
  updating: storeState.javnoPitanje.updating,
  updateSuccess: storeState.javnoPitanje.updateSuccess,
});

const mapDispatchToProps = {
  getDodatniInfoUsers,
  getArtikls,
  getOdgovorNaJavnoPitanjes,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(JavnoPitanjeUpdate);
